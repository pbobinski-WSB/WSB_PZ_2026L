import threading
import time
import random

# Globalne zdarzenie do sygnalizowania zamknięcia
shutdown_event = threading.Event()

class NativeProdConsPython:

    def __init__(self):
        self.pudelko_na_produkt = 0  
        self.condition = threading.Condition()

        # Zapisujemy referencje do wątków, aby móc na nie poczekać (join)
        self.konsument_thread = threading.Thread(target=self._petla_konsumenta, name="Konsument")
        self.producent_thread = threading.Thread(target=self._petla_producenta, name="Producent")
        
        self.konsument_thread.start()
        self.producent_thread.start()

    def _petla_konsumenta(self):
        """Nieskończona pętla dla wątku konsumenta, dopóki nie ma sygnału shutdown."""
        print(f"Wątek {threading.current_thread().name} wystartował.")
        while not shutdown_event.is_set():
            if not self._konsumuj(): # Jeśli _konsumuj zwróci False z powodu shutdown
                if shutdown_event.is_set():
                    break # Wyjdź z pętli
            # Małe opóźnienie, aby nie zapętlać się zbyt szybko, gdy nic się nie dzieje,
            # i dać szansę na przełączenie kontekstu, jeśli konsumpcja była bardzo szybka.
            # Usunięte, bo timeout w wait() oraz _sleeep() dają wystarczająco przerw.
            # if not shutdown_event.is_set(): time.sleep(0.01) 
        print(f"Wątek {threading.current_thread().name} zakończył pętlę.")

    def _petla_producenta(self):
        """Nieskończona pętla dla wątku producenta, dopóki nie ma sygnału shutdown."""
        print(f"Wątek {threading.current_thread().name} wystartował.")
        while not shutdown_event.is_set():
            if not self._produkuj(): # Jeśli _produkuj zwróci False z powodu shutdown
                if shutdown_event.is_set():
                    break
            # if not shutdown_event.is_set(): time.sleep(0.01)
        print(f"Wątek {threading.current_thread().name} zakończył pętlę.")

    def _konsumuj(self):
        """Logika konsumpcji jednego produktu. Zwraca False jeśli przerwane przez shutdown."""
        with self.condition:
            while self.pudelko_na_produkt == 0:
                if shutdown_event.is_set():
                    return False 
                
                notified = self.condition.wait(timeout=0.1) # Czekaj z timeoutem
                if not notified and shutdown_event.is_set(): # Timeout i sygnał zakończenia
                    return False
                # Jeśli notified=True, pętla sprawdzi warunek i shutdown_event ponownie
            
            if shutdown_event.is_set(): # Sprawdzenie po wyjściu z pętli while
                return False

            produkt = self.pudelko_na_produkt
            self.pudelko_na_produkt = 0
            
            print(f"Konsumuję {produkt}... ", end="", flush=True)
            self._sleeep() # Symulacja czasu pracy
            
            if shutdown_event.is_set(): # Sprawdzenie po _sleeep
                print("...konsumpcja przerwana.")
                # Nie powiadamiamy, bo zamykamy
                return False

            print("...skonsumowałem")
            self.condition.notify() # Powiadom producenta
            return True

    def _produkuj(self):
        """Logika produkcji jednego produktu. Zwraca False jeśli przerwane przez shutdown."""
        with self.condition:
            while self.pudelko_na_produkt != 0:
                if shutdown_event.is_set():
                    return False
                
                notified = self.condition.wait(timeout=0.1)
                if not notified and shutdown_event.is_set():
                    return False
            
            if shutdown_event.is_set():
                return False

            produkt = random.randint(1, 100) 
            print(f"Produkuję {produkt}... ", end="", flush=True)
            self._sleeep()

            if shutdown_event.is_set():
                print("...produkcja przerwana.")
                return False

            print("...wyprodukowałem")
            self.pudelko_na_produkt = produkt 
            self.condition.notify() # Powiadom konsumenta
            return True

    def _sleeep(self):
        """Symuluje pewien czas pracy, ale może zostać przerwany."""
        total_sleep_time_s = 0.05 + random.random() * 0.5
        # Podzielimy długi sen na krótsze interwały, aby szybciej reagować na shutdown
        sleep_interval_s = 0.05 # Sprawdzaj co 50ms
        slept_time_s = 0
        
        while slept_time_s < total_sleep_time_s:
            if shutdown_event.is_set():
                # print(f"{threading.current_thread().name}: Przerwano _sleeep z powodu shutdown.")
                return # Wyjdź wcześniej
            
            actual_sleep = min(sleep_interval_s, total_sleep_time_s - slept_time_s)
            time.sleep(actual_sleep)
            slept_time_s += actual_sleep

if __name__ == "__main__":
    app_instance = None # Inicjalizujemy na wypadek błędu przed utworzeniem instancji
    try:
        print("Uruchamiam producenta i konsumenta. Naciśnij Ctrl+C aby zakończyć.")
        app_instance = NativeProdConsPython() 
        
        while True:
            # Sprawdzanie, czy wątki robocze nadal działają
            if app_instance and (not app_instance.konsument_thread.is_alive() or \
                                 not app_instance.producent_thread.is_alive()):
                print("\nJeden z wątków (producent/konsument) zakończył się nieoczekiwanie.")
                # Dodatkowe informacje, który wątek się zakończył
                if not app_instance.konsument_thread.is_alive():
                    print("Wątek KONSUMENTA zakończony.")
                if not app_instance.producent_thread.is_alive():
                    print("Wątek PRODUCENTA zakończony.")
                break # Wyjdź z pętli głównego wątku
            time.sleep(1) # Utrzymuj główny wątek aktywnym i sprawdzaj co sekundę

    except KeyboardInterrupt:
        print("\nZażądano przerwania programu przez użytkownika (Ctrl+C).")
    except Exception as e: # Łapanie innych potencjalnych wyjątków w głównym bloku
        print(f"\nNieoczekiwany błąd w głównym wątku: {e}")
    finally:
        print("Sygnalizowanie wątkom zakończenia pracy...")
        shutdown_event.set() # Ustaw flagę, aby wątki zakończyły pętle

        if app_instance: # Upewnij się, że instancja i wątki zostały utworzone
            print("Oczekiwanie na zakończenie wątku konsumenta...")
            if hasattr(app_instance, 'konsument_thread') and app_instance.konsument_thread.is_alive():
                app_instance.konsument_thread.join(timeout=3) # Daj 3 sekundy
                if app_instance.konsument_thread.is_alive():
                    print("Wątek konsumenta NIE zakończył się w wyznaczonym czasie.")
                else:
                    print("Wątek konsumenta zakończony.")
            elif hasattr(app_instance, 'konsument_thread'):
                print("Wątek konsumenta już był zakończony.")


            print("Oczekiwanie na zakończenie wątku producenta...")
            if hasattr(app_instance, 'producent_thread') and app_instance.producent_thread.is_alive():
                app_instance.producent_thread.join(timeout=3)
                if app_instance.producent_thread.is_alive():
                    print("Wątek producenta NIE zakończył się w wyznaczonym czasie.")
                else:
                    print("Wątek producenta zakończony.")
            elif hasattr(app_instance, 'producent_thread'):
                 print("Wątek producenta już był zakończony.")
        
        print("Program zakończony.")