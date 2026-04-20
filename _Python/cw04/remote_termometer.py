import socket
import struct
import time
import random

def get_temperature():
    # Na potrzeby przykładu – losowa temperatura
    return round(random.uniform(18.0, 25.0), 2)

MULTICAST_GROUP = '228.222.222.222'
PORT = 10000
NAME = "Termometr-Kuchnia"

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
ttl = struct.pack('b', 1)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)

while True:
    temp = f"{get_temperature()} {NAME}"
    sock.sendto(temp.encode('utf-8'), (MULTICAST_GROUP, PORT))
    print("Wysłano:", temp)
    time.sleep(1)
