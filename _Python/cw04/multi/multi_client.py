import socket
import threading
import time
import sys
import random

HOST = 'localhost'
PORT = 9090
NUM_CLIENTS = 10  # Liczba klientów do uruchomienia
DELAY_SEC = 0.1   # Opóźnienie między startem kolejnych klientów (w sekundach)

successful_connections = 0
failed_connections = 0
lock = threading.Lock() # Do bezpiecznej modyfikacji liczników

# Funkcja wykonywana przez każdy wątek klienta
def run_client(client_id):
    global successful_connections, failed_connections
    thread_name = threading.current_thread().name
    print(f"[{thread_name}] Klient {client_id}: Uruchamianie...")

    try:
        # Używamy 'with', aby gniazdo zostało automatycznie zamknięte
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            # Ustawienie timeoutu na operacje gniazda (np. connect, recv)
            s.settimeout(5.0) # Czekaj max 5 sekund

            # 1. Połącz z serwerem
            s.connect((HOST, PORT))
            print(f"[{thread_name}] Klient {client_id}: Połączono z {HOST}:{PORT}")
            with lock:
                successful_connections += 1

            # 2. Przygotuj i wyślij wiadomość
            message = f"Wiadomość od klienta Python #{client_id}"
            print(f"[{thread_name}] Klient {client_id}: Wysyłanie: {message}")
            s.sendall(message.encode('utf-8'))

            # Opcjonalne małe opóźnienie przed odbiorem
            # time.sleep(random.uniform(0.1, 0.5))

            # 3. Odbierz odpowiedź
            try:
                data = s.recv(1024) # Blokuje do otrzymania danych lub timeoutu
                if data:
                    response = data.decode('utf-8').strip()
                    print(f"[{thread_name}] Klient {client_id}: Otrzymano odpowiedź: {response}")
                else:
                    # Serwer zamknął połączenie bez wysyłania danych (lub wysłał puste)
                    print(f"[{thread_name}] Klient {client_id}: Otrzymano pustą odpowiedź lub serwer zamknął połączenie.")
            except socket.timeout:
                print(f"[{thread_name}] Klient {client_id}: BŁĄD - Timeout podczas oczekiwania na odpowiedź.")
                with lock:
                    failed_connections +=1
            except ConnectionResetError:
                 print(f"[{thread_name}] Klient {client_id}: BŁĄD - Serwer zresetował połączenie podczas odbioru.")
                 with lock:
                    failed_connections += 1
            except Exception as e_recv:
                 print(f"[{thread_name}] Klient {client_id}: BŁĄD podczas odbioru: {e_recv}")
                 with lock:
                    failed_connections += 1


    except socket.timeout:
         print(f"[{thread_name}] Klient {client_id}: BŁĄD - Timeout podczas próby połączenia z {HOST}:{PORT}.")
         with lock:
            failed_connections += 1
    except ConnectionRefusedError:
        print(f"[{thread_name}] Klient {client_id}: BŁĄD - Nie można połączyć się z serwerem ({HOST}:{PORT}). Czy serwer działa?")
        with lock:
            failed_connections += 1
    except socket.error as e:
        print(f"[{thread_name}] Klient {client_id}: BŁĄD gniazda: {e}")
        with lock:
            failed_connections += 1
    except Exception as e_main:
        print(f"[{thread_name}] Klient {client_id}: Nieoczekiwany błąd: {e_main}")
        with lock:
            failed_connections += 1
    finally:
        print(f"[{thread_name}] Klient {client_id}: Zakończono.")


# Główna część skryptu
if __name__ == "__main__":
    print(f"Uruchamianie {NUM_CLIENTS} klientów testowych...")
    threads = []

    for i in range(NUM_CLIENTS):
        client_id = i + 1
        # Tworzymy wątek dla każdego klienta
        thread = threading.Thread(target=run_client, args=(client_id,), name=f"KlientWątek-{client_id}")
        threads.append(thread)
        thread.start() # Uruchamiamy wątek

        # Czekamy chwilę przed uruchomieniem następnego
        time.sleep(DELAY_SEC)

    print("\nWszystkie wątki klientów zostały uruchomione.")
    print("Oczekiwanie na zakończenie pracy klientów...")

    # Czekamy na zakończenie wszystkich wątków
    for t in threads:
        t.join()

    print("\n--- Podsumowanie Testu ---")
    print(f"Liczba uruchomionych klientów: {NUM_CLIENTS}")
    print(f"Udane połączenia: {successful_connections}")
    print(f"Nieudane połączenia/Błędy: {failed_connections}")
    print("--------------------------")
    print("Tester zakończył działanie.")