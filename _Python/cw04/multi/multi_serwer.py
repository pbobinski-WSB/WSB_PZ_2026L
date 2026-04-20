import selectors
import socket
import types # Do przechowywania danych powiązanych z gniazdem

HOST = 'localhost'
PORT = 9090

# Utwórz domyślny selektor (wybierze najlepszy dostępny mechanizm: epoll, kqueue, select)
sel = selectors.DefaultSelector()

# Funkcja do akceptowania nowych połączeń
def accept_wrapper(sock):
    conn, addr = sock.accept()  # Gotowe do akceptacji, nie zablokuje
    print(f"Zaakceptowano połączenie od {addr}")
    conn.setblocking(False)  # Ustaw gniazdo klienta na nieblokujące

    # Przygotuj obiekt do przechowywania danych dla tego połączenia
    data = types.SimpleNamespace(addr=addr, inb=b'', outb=b'')

    # Zarejestruj gniazdo klienta w selektorze do monitorowania zdarzeń odczytu i zapisu
    events = selectors.EVENT_READ | selectors.EVENT_WRITE # Monitorujemy oba
    sel.register(conn, events, data=data)

# Funkcja do obsługi danych od istniejących połączeń
def service_connection(key, mask):
    sock = key.fileobj  # Gniazdo powiązane z kluczem
    data = key.data     # Dane powiązane z gniazdem (nasz SimpleNamespace)

    # Jeśli gniazdo jest gotowe do odczytu
    if mask & selectors.EVENT_READ:
        try:
            recv_data = sock.recv(1024)  # Odczytaj dane (nie blokuje)
        except ConnectionResetError:
            print(f"Klient {data.addr} nagle zamknął połączenie.")
            sel.unregister(sock)
            sock.close()
            return

        if recv_data:
            # Mamy dane, dodaj je do bufora wejściowego
            message = recv_data.decode('utf-8').strip()
            print(f"Otrzymano od {data.addr}: {message}")
            # Przygotuj odpowiedź (echo) i dodaj do bufora wyjściowego
            data.outb += f"Echo: {message}\n".encode('utf-8')
        else:
            # Klient wysłał puste dane -> zamyka połączenie
            print(f"Klient {data.addr} zamknął połączenie.")
            sel.unregister(sock) # Wyrejestruj gniazdo
            sock.close()         # Zamknij gniazdo

    # Jeśli gniazdo jest gotowe do zapisu i mamy coś do wysłania
    if mask & selectors.EVENT_WRITE:
        if data.outb:
            print(f"Wysyłanie do {data.addr}: {data.outb.decode('utf-8').strip()}")
            try:
                sent = sock.send(data.outb)  # Spróbuj wysłać (nie blokuje)
                data.outb = data.outb[sent:] # Usuń wysłane bajty z bufora
            except BrokenPipeError:
                 print(f"Błąd wysyłania (BrokenPipe) do {data.addr}. Zamykanie.")
                 sel.unregister(sock)
                 sock.close()


# --- Główna część serwera ---

lsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
lsock.bind((HOST, PORT))
lsock.listen()
print(f"Serwer Python (selectors) nasłuchuje na {HOST}:{PORT}...")
lsock.setblocking(False) # Gniazdo nasłuchujące musi być nieblokujące

# Zarejestruj gniazdo nasłuchujące w selektorze, aby monitorować nowe połączenia (EVENT_READ)
# Nie przekazujemy danych (data=None), bo to specjalne gniazdo
sel.register(lsock, selectors.EVENT_READ, data=None)

try:
    # Pętla główna serwera
    while True:
        # Czekaj na zdarzenia (blokuje do czasu wystąpienia zdarzenia)
        # timeout=None oznacza czekanie w nieskończoność
        events = sel.select(timeout=None)

        # Przetwarzaj gotowe gniazda
        for key, mask in events:
            if key.data is None:
                # To jest gniazdo nasłuchujące - zaakceptuj nowe połączenie
                accept_wrapper(key.fileobj)
            else:
                # To jest gniazdo klienta - obsłuż dane
                service_connection(key, mask)

except KeyboardInterrupt:
    print("Serwer zatrzymany przez użytkownika (Ctrl+C).")
finally:
    print("Zamykanie selektora.")
    sel.close()
    print("Zamykanie gniazda nasłuchującego.")
    lsock.close()
    print("Serwer Python zakończył działanie.")