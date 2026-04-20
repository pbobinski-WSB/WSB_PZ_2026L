import socket

HOST = 'localhost'
PORT = 9090

print(f"Serwer Python nasłuchuje na {HOST}:{PORT} (tryb blokujący)...")

# Używamy 'with', aby automatycznie zamknąć gniazdo
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen() # Rozpocznij nasłuchiwanie

    # Czekaj na połączenie klienta (blokujące)
    conn, addr = s.accept()
    with conn: # Używamy 'with' dla gniazda klienta
        print(f"Klient połączony: {addr}")

        # Odbierz dane (blokujące)
        data = conn.recv(1024) # Odczytaj do 1024 bajtów
        if data:
            message = data.decode('utf-8')
            print(f"Otrzymano od klienta: {message}")

            # Przygotuj i wyślij odpowiedź (echo)
            response = f"Serwer Python otrzymał: {message}"
            conn.sendall(response.encode('utf-8')) # Wyślij wszystko
            print("Wysłano odpowiedź do klienta.")
        else:
             print("Klient rozłączył się bez wysyłania danych.")

print("Serwer Python zakończył działanie.")