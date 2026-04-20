### Tekstowy odczyt/zapis
# Zapis do pliku
with open("hello.txt", "w", encoding="utf-8") as file:
    file.write("Hello, Python IO!")

# Odczyt z pliku
with open("hello.txt", "r", encoding="utf-8") as file:
    line = file.readline()
    print("Odczytano:", line)

### Binarne IO# Zapis binarny

import struct

with open("data.bin", "wb") as file:
    file.write((42).to_bytes(4, byteorder='big'))
    file.write(bytearray(struct.pack("d", 3.14)))  # wymaga importu struct

# Odczyt binarny

with open("data.bin", "rb") as file:
    liczba = int.from_bytes(file.read(4), byteorder='big')
    pi = struct.unpack("d", file.read(8))[0]
    print("Liczba:", liczba, ", PI:", pi)

### Dekorator w PythoniePython nie używa wzorca dekoratora w IO tak jawnie, ale buforowanie też istnieje:
with open("../data.txt", "r", buffering=8192) as file:
    print(file.readline())

### Obsługa wyjątków
try:
    with open("../data.txt", "r") as file:
        print(file.readline())
except FileNotFoundError:
    print("Nie znaleziono pliku.")
except IOError as e:
    print("Błąd IO:", e)

### Pathlib – nowoczesna obsługa ścieżekfrom pathlib import Path
from pathlib import Path

path = Path("hello.txt")
path.write_text("Zapis z Pathlib")
print("Odczyt:", path.read_text())
