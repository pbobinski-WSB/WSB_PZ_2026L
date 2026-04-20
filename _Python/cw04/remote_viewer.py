import socket
import struct

MULTICAST_GROUP = '228.222.222.222'
PORT = 10000

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

sock.bind(('', PORT))  # nasłuchujemy na wszystkich interfejsach

group = socket.inet_aton(MULTICAST_GROUP)
mreq = struct.pack('4sL', group, socket.INADDR_ANY)

sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

print(f"Odbiornik nasłuchuje na {MULTICAST_GROUP}:{PORT}")

while True:
    data, addr = sock.recvfrom(1024)
    print(f"Odebrano od {addr}: {data.decode('utf-8')}")
