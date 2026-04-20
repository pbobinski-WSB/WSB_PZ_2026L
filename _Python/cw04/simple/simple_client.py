import socket

HOST = 'localhost'
PORT = 9090

# Używamy 'with', aby automatycznie zamknąć gniazdo
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    try:
        # Połącz z serwerem (blokujące)
        s.connect((HOST, PORT))
        print(f"Połączono z serwerem {HOST}:{PORT}")

        # Pobierz wiadomość od użytkownika
        message = input("Wpisz wiadomość do wysłania: ")

        # Wyślij wiadomość (zakodowaną jako bajty)
        s.sendall(message.encode('utf-8'))
        print(f"Wysłano: {message}")

        # Odbierz odpowiedź serwera (blokujące)
        data = s.recv(1024)
        if data:
            response = data.decode('utf-8')
            print(f"Otrzymano od serwera: {response}")
        else:
            print("Serwer zamknął połączenie przed wysłaniem odpowiedzi.")

    except ConnectionRefusedError:
        print(f"Nie można połączyć się z serwerem {HOST}:{PORT}. Czy serwer jest uruchomiony?")
    except Exception as e:
        print(f"Wystąpił błąd klienta: {e}")

print("Klient Python zakończył działanie.")