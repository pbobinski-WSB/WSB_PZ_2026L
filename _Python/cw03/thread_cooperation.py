import threading
import time
import random
from concurrent.futures import ThreadPoolExecutor
import sys

# Zdarzenie do sygnalizowania zakończenia pracy wątków
shutdown_event = threading.Event()

class Account:
    def __init__(self):
        self._balance = 0
        self._lock = threading.RLock()
        self._new_deposit_condition = threading.Condition(self._lock)

    def get_balance(self):
        return self._balance

    def withdraw(self, amount):
        # Dodajemy sprawdzenie shutdown_event przed próbą zdobycia blokady
        if shutdown_event.is_set():
            # print("Wątek wypłaty: Zauważono sygnał zakończenia przed blokadą.")
            return False # Sygnalizujemy, że operacja nie została wykonana

        with self._lock:
            try:
                while self._balance < amount:
                    if shutdown_event.is_set():
                        # print("Wątek wypłaty: Zauważono sygnał zakończenia w trakcie oczekiwania.")
                        return False # Sygnalizujemy, że operacja nie została wykonana
                    
                    # print(f"\t\t\tCzekaj na wpłatę ({amount} > {self._balance})")
                    # Czekamy z timeoutem, aby móc okresowo sprawdzać shutdown_event
                    # Jeśli wait() zostanie przerwany (np. przez notify), to dobrze.
                    # Jeśli timeout, sprawdzimy shutdown_event.
                    notified = self._new_deposit_condition.wait(timeout=0.1) # Czekaj maks. 0.1s
                    if not notified and shutdown_event.is_set(): # Timeout i sygnał zakończenia
                        # print("Wątek wypłaty: Timeout i zauważono sygnał zakończenia.")
                        return False
                
                # Sprawdzenie po przebudzeniu, na wypadek gdyby sygnał przyszedł między wait a teraz
                if shutdown_event.is_set():
                    return False

                self._balance -= amount
                print(f"\t\t\tWypłata {amount}\t\t{self.get_balance()}")
                return True # Operacja zakończona sukcesem

            except InterruptedError:
                print("Wątek wypłaty przerwany podczas oczekiwania.")
                return False
            except Exception as e:
                print(f"Nieoczekiwany błąd w withdraw: {e}")
                return False

    def deposit(self, amount):
        if shutdown_event.is_set():
            # print("Wątek wpłaty: Zauważono sygnał zakończenia przed blokadą.")
            return False

        with self._lock:
            try:
                # Sprawdzenie na wypadek, gdyby sygnał przyszedł, gdy wątek czekał na lock
                if shutdown_event.is_set():
                    return False

                self._balance += amount
                print(f"Wpłata {amount}\t\t\t\t\t{self.get_balance()}")
                self._new_deposit_condition.notify_all()
                return True
            except Exception as e:
                print(f"Nieoczekiwany błąd w deposit: {e}")
                return False

account = Account()

def deposit_task():
    print(f"Wątek {threading.current_thread().name} (wpłata) wystartował.")
    try:
        # Pętla działa dopóki shutdown_event nie jest ustawiony
        while not shutdown_event.is_set():
            amount_to_deposit = random.randint(1, 10)
            if not account.deposit(amount_to_deposit):
                if shutdown_event.is_set(): # Jeśli deposit zwrócił False z powodu shutdown
                    break 
            # Czekamy tylko jeśli shutdown_event nie jest ustawiony
            if not shutdown_event.is_set():
                time.sleep(1) # Odpowiednik Thread.sleep(1000)
    except Exception as e: # Ogólny wyjątek
        if not isinstance(e, KeyboardInterrupt): # Nie drukuj dla KeyboardInterrupt
             print(f"Błąd w zadaniu wpłaty ({threading.current_thread().name}): {e}")
    finally:
        print(f"Wątek {threading.current_thread().name} (wpłata) zakończył pracę.")


def withdraw_task():
    print(f"Wątek {threading.current_thread().name} (wypłata) wystartował.")
    try:
        while not shutdown_event.is_set():
            amount_to_withdraw = random.randint(1, 10)
            if not account.withdraw(amount_to_withdraw):
                if shutdown_event.is_set():
                    break
            # Małe opóźnienie, aby nie "spamować" próbami wypłaty, jeśli konto jest często puste
            # i aby dać szansę na zauważenie shutdown_event
            if not shutdown_event.is_set():
                 time.sleep(random.uniform(0.01, 0.2)) # Czekaj losowy krótki czas
    except Exception as e:
        if not isinstance(e, KeyboardInterrupt):
            print(f"Błąd w zadaniu wypłaty ({threading.current_thread().name}): {e}")
    finally:
        print(f"Wątek {threading.current_thread().name} (wypłata) zakończył pracę.")


if __name__ == "__main__":
    print("Wątek wpłaty\t\tWątek wypłaty\t\tSaldo")
    print("-" * 60)

    # Używamy max_workers=2, aby stworzyć pulę z dwoma wątkami
    # nadajemy nazwy wątkom w puli dla lepszego debugowania
    executor = ThreadPoolExecutor(
        max_workers=2, 
        thread_name_prefix='KontoThread'
    )
    
    try:
        # Przesyłamy zadania do puli
        future_deposit = executor.submit(deposit_task)
        future_withdraw = executor.submit(withdraw_task)

        # Pozwalamy programowi działać przez określony czas lub do przerwania
        # Na przykład, pozwólmy mu działać przez 10 sekund
        # time.sleep(10) 
        # Lub, czekaj na Ctrl+C
        while True:
            time.sleep(0.5) # Utrzymuj główny wątek przy życiu
            # Można tu dodać logikę sprawdzania, czy któryś z wątków się nieoczekiwanie zakończył
            if future_deposit.done() or future_withdraw.done():
                print("Jedno z zadań zakończyło się przedwcześnie.")
                if future_deposit.done() and future_deposit.exception():
                     print(f"Wyjątek w zadaniu wpłaty: {future_deposit.exception()}")
                if future_withdraw.done() and future_withdraw.exception():
                     print(f"Wyjątek w zadaniu wypłaty: {future_withdraw.exception()}")
                break

    except KeyboardInterrupt:
        print("\nZażądano przerwania programu (Ctrl+C)...")
    finally:
        print("Sygnalizowanie wątkom zakończenia pracy...")
        shutdown_event.set() # Ustawiamy zdarzenie, informując wątki, aby się zakończyły

        # Sprawdzenie wersji Pythona, aby użyć cancel_futures jeśli dostępne (Python 3.9+)
        if sys.version_info >= (3, 9):
            print("(Używam shutdown z cancel_futures=True)")
            executor.shutdown(wait=True, cancel_futures=True)
        else:
            print("(Używam shutdown bez cancel_futures)")
            executor.shutdown(wait=True)
        
        print("Pula wątków zamknięta.")
        print("Program zakończony.")