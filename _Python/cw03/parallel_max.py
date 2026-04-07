import concurrent.futures
import os
import time
import random # Do ewentualnych testów z losowymi danymi

# Stałe
N_VAL = 9_000_000
# Zwiększamy THRESHOLD, aby zmniejszyć liczbę zadań i narzut komunikacji
# Dla N_VAL = 9M i THRESHOLD = 100k, będziemy mieli 90 zadań.
# Dla N_VAL = 9M i THRESHOLD = 1M, będziemy mieli 9 zadań.
# Warto poeksperymentować z tą wartością.
THRESHOLD = 500_000

# Zmienna globalna, która zostanie ustawiona w każdym procesie roboczym
# Będzie przechowywać referencję do głównej listy danych.
worker_shared_list = None

def init_worker_process(main_list_data):
    """
    Funkcja inicjalizująca dla każdego procesu roboczego.
    Kopiuje (lub na systemach z fork, współdzieli przez CoW) główną listę danych
    do globalnej zmiennej w przestrzeni adresowej procesu roboczego.
    """
    global worker_shared_list
    worker_shared_list = main_list_data
    # print(f"Proces roboczy {os.getpid()} zainicjalizowany.") # Do debugowania

def find_max_in_slice_worker(indices_tuple):
    """
    Funkcja wykonywana przez procesy robocze.
    Znajduje maksimum w fragmencie listy (worker_shared_list)
    określonym przez (low_index, high_index).
    """
    global worker_shared_list
    low, high = indices_tuple

    if worker_shared_list is None:
        # To nie powinno się zdarzyć, jeśli initializer działa poprawnie
        raise Exception(f"Błąd: worker_shared_list nie została zainicjalizowana w procesie {os.getpid()}")

    if low >= high: # Pusty lub niepoprawny zakres
        return float('-inf')

    # Poprawne zainicjowanie maksimum dla danego zakresu
    current_max = worker_shared_list[low]
    for i in range(low + 1, high): # Pętla od drugiego elementu w zakresie
        if worker_shared_list[i] > current_max:
            current_max = worker_shared_list[i]
    return current_max

def get_parallel_max(main_data_list):
    n = len(main_data_list)
    if n == 0:
        return float('-inf')

    # Jeśli lista jest mała, przetwarzaj sekwencyjnie
    # np. jeśli mniej niż 2 pełne fragmenty (THRESHOLDY)
    if n < THRESHOLD * (os.cpu_count() or 1) : # Mniejszy próg dla przetwarzania sekwencyjnego
        print("(Info: Przetwarzanie sekwencyjne dla tej listy)")
        return max(main_data_list) if main_data_list else float('-inf')

    num_processes = os.cpu_count() or 1 # Użyj dostępnych rdzeni

    # Przygotowanie argumentów dla zadań (tylko krotki z indeksami)
    task_slice_indices = []
    for i in range(0, n, THRESHOLD):
        low = i
        high = min(i + THRESHOLD, n)
        if low < high: # Upewnij się, że fragment nie jest pusty
            task_slice_indices.append((low, high))
    
    if not task_slice_indices: # Jeśli nie ma fragmentów
        return max(main_data_list) if main_data_list else float('-inf')

    partial_max_values = []
    # Używamy ProcessPoolExecutor
    # `initializer` i `initargs` są kluczowe do jednorazowego przekazania danych
    with concurrent.futures.ProcessPoolExecutor(
            max_workers=num_processes,
            initializer=init_worker_process,
            initargs=(main_data_list,)) as executor:
        try:
            # `executor.map` jest wygodne - przekazuje każdy element z `task_slice_indices`
            # do funkcji `find_max_in_slice_worker`.
            # Zwraca iterator wyników w kolejności zadań.
            results_iterator = executor.map(find_max_in_slice_worker, task_slice_indices)
            
            # Pobierz wszystkie wyniki (to zablokuje, dopóki wszystkie zadania się nie zakończą)
            partial_max_values = list(results_iterator)

        except Exception as e:
            print(f"Wystąpił błąd podczas przetwarzania równoległego: {e}")
            print("(Info: Powrót do przetwarzania sekwencyjnego z powodu błędu puli)")
            return max(main_data_list) if main_data_list else float('-inf')

    if not partial_max_values:
        print("(Info: Brak wyników z przetwarzania równoległego, fallback do sekwencyjnego)")
        return max(main_data_list) if main_data_list else float('-inf')

    # Znajdź ostateczne maksimum z częściowych wyników
    return max(partial_max_values)


# --- Główna część skryptu ---
if __name__ == "__main__":
    print(f"Używany THRESHOLD: {THRESHOLD}")
    print(f"Przygotowywanie listy o rozmiarze {N_VAL}...")
    # Oryginalny kod Javy: list[i] = i;
    data_list = list(range(N_VAL))
    # Można przetestować z bardziej losowymi danymi:
    # data_list = [random.randint(0, N_VAL * 2) for _ in range(N_VAL)]
    # data_list[N_VAL // 3] = N_VAL * 3 # Upewnij się, że maksimum jest gdzieś w środku
    print("Lista przygotowana.")

    # Pomiar czasu dla implementacji równoległej
    print("\nUruchamianie przetwarzania równoległego...")
    start_time_parallel = time.perf_counter()
    maximal_number_parallel = get_parallel_max(data_list)
    end_time_parallel = time.perf_counter()
    time_taken_parallel_ms = (end_time_parallel - start_time_parallel) * 1000

    print(f"\nMaksymalna liczba (równolegle) to {maximal_number_parallel}")
    print(f"Liczba procesorów (rdzeni logicznych) użyta: {os.cpu_count() or 'nieznana'}")
    print(f"Czas przetwarzania równoległego: {time_taken_parallel_ms:.2f} milisekund")

    # Pomiar czasu dla implementacji czysto sekwencyjnej (używając wbudowanej funkcji max)
    print("\nUruchamianie przetwarzania sekwencyjnego dla porównania (Python's max())...")
    start_time_seq = time.perf_counter()
    maximal_number_seq = max(data_list) if data_list else float('-inf') # Najprostszy i szybki sposób
    end_time_seq = time.perf_counter()
    time_taken_seq_ms = (end_time_seq - start_time_seq) * 1000
    
    print(f"\nMaksymalna liczba (sekwencyjnie, Python's max()) to {maximal_number_seq}")
    print(f"Czas przetwarzania sekwencyjnego (Python's max()): {time_taken_seq_ms:.2f} milisekund")

    if maximal_number_parallel != maximal_number_seq:
        print(f"BŁĄD KRYTYCZNY: Wynik równoległy ({maximal_number_parallel}) różni się od sekwencyjnego ({maximal_number_seq})!")
    else:
        print("Wyniki równoległy i sekwencyjny są zgodne.")

    if time_taken_seq_ms > 0 and time_taken_parallel_ms > 0:
        speedup = time_taken_seq_ms / time_taken_parallel_ms
        print(f"\nPrzyśpieszenie (względem Python's max()): {speedup:.2f}x")
    else:
        print("\nNie można obliczyć przyśpieszenia (jeden z czasów wynosił zero lub był ujemny).")