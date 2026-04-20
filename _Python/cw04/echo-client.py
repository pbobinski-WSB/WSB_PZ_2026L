import socket

HOST = "127.0.0.1"
PORT = 12345

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
    sock.connect((HOST, PORT))
    sock.sendall(b"Czesc serwerze!")
    data = sock.recv(1024)

print("Odpowiedź serwera:", data.decode())
