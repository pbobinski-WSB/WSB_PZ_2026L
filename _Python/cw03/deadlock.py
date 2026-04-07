import threading
import time

class DeadlockExamplePython:

    def __init__(self):
        # Odpowiednik ReentrantLock(true) - używamy RLock dla reentrancji,
        # chociaż w tym konkretnym przykładzie zwykły Lock też by zadziałał.
        # Pythonowe Locki nie mają bezpośredniego argumentu 'fairness' jak w Javie.
        self.lock1 = threading.RLock()
        self.lock2 = threading.RLock()

    def _print_message(self, message):
        """Pomocnicza funkcja do drukowania z nazwą wątku."""
        print(f"Wątek {threading.current_thread().name}: {message}")

    def _sleep_ms(self, millis):
        """Pomocnicza funkcja do uśpienia wątku, przyjmuje milisekundy."""
        try:
            time.sleep(millis / 1000.0) # time.sleep przyjmuje sekundy
        except InterruptedError: # Rzadko używane wprost w Pythonie jak InterruptedException
            # W Pythonie, sleep może być przerwany przez sygnał (np. KeyboardInterrupt)
            print(f"Wątek {threading.current_thread().name} przerwany podczas snu.")


    def operation1(self):
        self._print_message("próbuje zdobyć lock1.")
        self.lock1.acquire()
        self._print_message("lock1 zdobyty, czeka na zdobycie lock2.")
        self._sleep_ms(50)

        self._print_message("próbuje zdobyć lock2.")
        # W tym miejscu T1 będzie czekał, jeśli T2 zdobył lock2 i czeka na lock1
        self.lock2.acquire() 
        self._print_message("lock2 zdobyty (z operation1).") # Ten komunikat nie powinien się pojawić w deadlocku

        self._print_message("wykonywanie pierwszej operacji.")

        self.lock2.release()
        self._print_message("lock2 zwolniony (z operation1).")
        self.lock1.release()
        self._print_message("lock1 zwolniony (z operation1).")

    def operation2(self):
        self._print_message("próbuje zdobyć lock2.")
        self.lock2.acquire()
        self._print_message("lock2 zdobyty, czeka na zdobycie lock1.")
        self._sleep_ms(50)

        self._print_message("próbuje zdobyć lock1.")
        # W tym miejscu T2 będzie czekał, jeśli T1 zdobył lock1 i czeka na lock2
        self.lock1.acquire() 
        self._print_message("lock1 zdobyty (z operation2).") # Ten komunikat nie powinien się pojawić w deadlocku

        self._print_message("wykonywanie drugiej operacji.")

        self.lock1.release()
        self._print_message("lock1 zwolniony (z operation2).")
        self.lock2.release()
        self._print_message("lock2 zwolniony (z operation2).")


if __name__ == "__main__":
    deadlock_app = DeadlockExamplePython()

    # Tworzenie i uruchamianie wątków
    # target=deadlock_app.operation1 jest odpowiednikiem deadlock::operation1
    thread1 = threading.Thread(target=deadlock_app.operation1, name="T1")
    thread2 = threading.Thread(target=deadlock_app.operation2, name="T2")

    print("Uruchamianie wątków, które powinny doprowadzić do zakleszczenia...")
    thread1.start()
    thread2.start()

    # Wątki powinny się zakleszczyć, a program nie zakończy się normalnie.
    # Aby zobaczyć, że "wiszą", można dodać join z timeoutem, ale dla demonstracji
    # zakleszczenia, pozwolenie im na działanie (i zawieszenie programu) jest wystarczające.
    # Program trzeba będzie przerwać ręcznie (np. Ctrl+C).
    
    # Opcjonalne: Jeśli chcesz, aby główny wątek poczekał (i zobaczył, że się zawiesił)
    # Można dać timeout, żeby program nie wisiał w nieskończoność w przypadku testów automatycznych
    # print("Główny wątek czeka na T1...")
    # thread1.join(timeout=5) # Czekaj maksymalnie 5 sekund
    # print("Główny wątek czeka na T2...")
    # thread2.join(timeout=5)
    
    # if thread1.is_alive() or thread2.is_alive():
    #     print("\nJeden lub oba wątki wciąż działają - prawdopodobnie zakleszczenie!")
    # else:
    #     print("\nWszystkie wątki zakończyły się (co nie powinno się zdarzyć w przypadku zakleszczenia).")

    # Aby program się nie zakończył od razu, jeśli joiny nie są używane:
    # (Chociaż w przypadku deadlocka i tak by nie doszło do tego miejsca normalnie)
    # while thread1.is_alive() or thread2.is_alive():
    #     try:
    #         time.sleep(0.1)
    #     except KeyboardInterrupt:
    #         print("\nPrzerwano przez użytkownika.")
    #         break
    print("\nKoniec programu (jeśli nie było zakleszczenia lub został przerwany).")
    print("Jeśli program wciąż działa, oznacza to zakleszczenie.")