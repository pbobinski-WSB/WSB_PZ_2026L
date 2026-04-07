import threading
import time
import sys # Dla sys.version_info

# Globalne zdarzenie do sygnalizowania zamknięcia
shutdown_event = threading.Event()

class Message:
    def __init__(self, msg_str):
        self._msg = msg_str

    def get_msg(self):
        return self._msg

    def set_msg(self, msg_str):
        self._msg = msg_str

class Waiter(threading.Thread):
    def __init__(self, msg_obj, condition, name=None):
        super().__init__(name=name)
        self.msg_obj = msg_obj
        self.condition = condition

    def run(self):
        name = threading.current_thread().name
        print(f"Wątek {name} wystartował.")
        with self.condition:
            try:
                # Czekaj na powiadomienie lub sygnał shutdown
                # Dopóki nie ma sygnału shutdown i nie zostaliśmy powiadomieni (np. przez zmianę msg_obj)
                # To jest uproszczenie; w rzeczywistości `wait` nie zwraca informacji, czy był notify.
                # Musimy polegać na tym, że Notifier zmieni msg_obj, a my sprawdzimy to po wait.
                # Dla demonstracji przerwania przez shutdown, ważny jest timeout w wait.
                
                initial_msg_content = self.msg_obj.get_msg() # Zapamiętaj stan przed wait

                while not shutdown_event.is_set() and self.msg_obj.get_msg() == initial_msg_content:
                    print(f"{name} czeka na powiadomienie o {time.time() * 1000:.0f}")
                    # Czekaj z timeoutem, aby móc okresowo sprawdzać shutdown_event
                    # Jeśli wait zostanie przerwany (notified), pętla while sprawdzi warunek.
                    # Jeśli timeout, pętla while sprawdzi shutdown_event.
                    notified_within_timeout = self.condition.wait(timeout=0.5) # Czekaj maks. 0.5s
                    
                    if shutdown_event.is_set():
                        print(f"{name}: Zauważono sygnał zakończenia podczas oczekiwania lub po timeout.")
                        break # Wyjdź z pętli while (i z with self.condition)

                    if notified_within_timeout:
                        # Zostaliśmy powiadomieni, sprawdźmy, czy warunek (zmiana wiadomości) jest spełniony
                        # Pętla while to zrobi automatycznie.
                        # print(f"{name} został obudzony przez notify().")
                        pass 
                    # else:
                        # Timeout, pętla sprawdzi shutdown_event i warunek wiadomości.
                        # print(f"{name}: Timeout podczas oczekiwania.")


                if shutdown_event.is_set():
                    print(f"{name} kończy pracę z powodu sygnału shutdown.")
                    return # Zakończ metodę run

                # Jeśli wyszliśmy z pętli, a shutdown_event nie jest ustawiony,
                # to znaczy, że zostaliśmy powiadomieni i wiadomość się zmieniła.
                print(f"{name} wątek kelnera został powiadomiony o {time.time() * 1000:.0f}")
                print(f"{name} przetworzono: {self.msg_obj.get_msg()}")

            except InterruptedError: # Bardziej teoretyczne w Pythonie dla wait()
                print(f"Wątek {name} został przerwany (InterruptedError).")
            except Exception as e:
                print(f"Wątek {name} napotkał błąd: {e}")
        print(f"Wątek {name} zakończył pracę.")


class Notifier(threading.Thread):
    def __init__(self, msg_obj, condition, name=None):
        super().__init__(name=name)
        self.msg_obj = msg_obj
        self.condition = condition

    def run(self):
        name = threading.current_thread().name
        print(f"Wątek {name} wystartował.")
        try:
            # Czekaj tylko jeśli shutdown_event nie jest ustawiony
            # Dzielimy sleep, aby szybciej reagować na shutdown
            total_sleep_time = 1.0 # 1 sekunda
            sleep_interval = 0.1
            slept_time = 0
            while slept_time < total_sleep_time and not shutdown_event.is_set():
                time.sleep(min(sleep_interval, total_sleep_time - slept_time))
                slept_time += sleep_interval
            
            if shutdown_event.is_set():
                print(f"{name} kończy pracę z powodu sygnału shutdown przed wysłaniem powiadomienia.")
                return

            with self.condition:
                if shutdown_event.is_set(): # Sprawdź ponownie po zdobyciu blokady
                    print(f"{name} kończy pracę z powodu sygnału shutdown (wewnątrz bloku).")
                    return

                new_message_content = f"{name} Notifier zakończył pracę"
                self.msg_obj.set_msg(new_message_content)
                print(f"{name} ustawił wiadomość na: '{new_message_content}' i wysyła powiadomienie.")
                
                self.condition.notify() # Powiadamia JEDEN czekający wątek
                # self.condition.notify_all() # Powiadomiłby WSZYSTKIE czekające wątki
        except Exception as e:
            print(f"Wątek {name} napotkał błąd: {e}")
        print(f"Wątek {name} zakończył pracę.")


if __name__ == "__main__":
    threads_list = []
    try:
        msg = Message("przetwórz to")
        condition = threading.Condition()

        waiter_thread = Waiter(msg_obj=msg, condition=condition, name="kelner")
        threads_list.append(waiter_thread)
        
        waiter_thread1 = Waiter(msg_obj=msg, condition=condition, name="kelner1")
        threads_list.append(waiter_thread1)

        notifier_thread = Notifier(msg_obj=msg, condition=condition, name="powiadamiający")
        threads_list.append(notifier_thread)

        print("Uruchamianie wszystkich wątków...")
        for t in threads_list:
            t.start()
        
        print("Wszystkie wątki zostały uruchomione.")

        # Główny wątek czeka na Ctrl+C lub na zakończenie wszystkich wątków roboczych
        # (co może się nie zdarzyć, jeśli kelnerzy czekają bez końca)
        while any(t.is_alive() for t in threads_list):
            time.sleep(0.5) # Sprawdzaj co pół sekundy
            # Można tu dodać logikę np. po określonym czasie ustawienia shutdown_event,
            # jeśli nie chcemy czekać na Ctrl+C

    except KeyboardInterrupt:
        print("\nZażądano przerwania programu (Ctrl+C)...")
    except Exception as e:
        print(f"\nNieoczekiwany błąd w głównym wątku: {e}")
    finally:
        print("Sygnalizowanie wątkom zakończenia pracy...")
        shutdown_event.set() # Ustaw flagę, aby wątki zakończyły pętle

        # Ważne: po ustawieniu shutdown_event, jeśli wątki są w condition.wait(),
        # muszą albo wyjść z niego z powodu timeoutu, albo zostać obudzone przez notify/notify_all,
        # aby mogły sprawdzić shutdown_event.
        # Jeśli używamy notify() i jest więcej kelnerów niż powiadomień,
        # niektórzy mogą pozostać w wait(). Timeout w wait() jest kluczowy.
        # Można też dodać dodatkowe `notify_all` tutaj, aby "obudzić" wszystkich,
        # którzy mogli utknąć w `wait` (choć powinni wyjść przez timeout).
        # with condition:
        #     print("Główny wątek: wysyłanie notify_all() na wszelki wypadek.")
        #     condition.notify_all()


        print("Oczekiwanie na zakończenie wątków...")
        for t in threads_list:
            thread_name = t.name if hasattr(t, 'name') else "Nieznany wątek"
            if t.is_alive():
                print(f"Czekam na {thread_name}...")
                t.join(timeout=2) # Daj 2 sekundy na zakończenie
                if t.is_alive():
                    print(f"Wątek {thread_name} NIE zakończył się w wyznaczonym czasie.")
                else:
                    print(f"Wątek {thread_name} zakończony.")
            else:
                print(f"Wątek {thread_name} już był zakończony.")
        
        print("Program zakończony.")