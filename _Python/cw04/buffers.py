import io

# 1. Przygotowanie bufora (BytesIO w pamięci)
buffer = io.BytesIO()
print("--- Przygotowanie bufora ---")
print(f"Pozycja: {buffer.tell()}")
print(f"Rozmiar (początkowy): {buffer.getbuffer().nbytes}")

# Dane do zapisania
data_to_write = b"Hello, NIO-like world in Python!"

# 2. Wypełnienie bufora danymi (zapis do BytesIO)
buffer.write(data_to_write)
print(f"\n--- Wypełnienie bufora danymi ({len(data_to_write)} bajtów zapisano) ---")
print(f"Pozycja po zapisie: {buffer.tell()}")
print(f"Rozmiar po zapisie: {buffer.getbuffer().nbytes}")

# 3. Odpowiednik flip() - ustawienie pozycji na początek do odczytu
buffer.seek(0)
print("\n--- Ustawienie pozycji na początek (seek(0)) ---")
print(f"Pozycja po seek(0): {buffer.tell()}")

# 4. Opróżnianie bufora (odczyt z BytesIO)
print("\n--- Opróżnianie bufora ---")
read_data = buffer.read()
print(f"Odczytane dane: {read_data.decode()}")
print(f"Pozycja po odczycie: {buffer.tell()}")

# Przygotowanie bufora do ponownego użycia (odpowiednik clear() - resetowanie pozycji)
buffer.seek(0)
buffer.truncate(0)  # Opcjonalnie wyczyszczenie zawartości
print("\n--- Przygotowanie bufora do ponownego użycia (seek(0), truncate(0)) ---")
print(f"Pozycja po resecie: {buffer.tell()}")
print(f"Rozmiar po resecie: {buffer.getbuffer().nbytes}")

buffer.close()