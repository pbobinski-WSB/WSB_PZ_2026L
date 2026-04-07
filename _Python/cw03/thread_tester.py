import threading
import time # Choć nieużywany wprost w logice, często przydatny przy wątkach

class ThreadTester2(threading.Thread):
    # Odpowiednik "private static final int N = 5;"
    # W Pythonie, atrybuty klasy są dostępne dla wszystkich instancji
    N = 5

    def __init__(self):
        super().__init__() # Wywołanie konstruktora klasy bazowej (threading.Thread)
        # Odpowiednik "private int i;" - w Pythonie atrybuty instancji tworzymy w __init__
        # lub przy pierwszym użyciu z 'self'.
        # W tym przypadku 'i' jest efektywnie zmienną sterującą pętli w 'run',
        # więc nie musi być jawnie deklarowana jako atrybut instancji wcześniej,
        # ale dla lepszego odwzorowania Javy, można by tu napisać self.i = 0
        # jednak pętla for i tak nada jej wartości.
        # W tym przykładzie pętla for 'self.i in range(...)' zrobi to samo co w Javie.

    def run(self):
        # W Javie 'i' było atrybutem instancji.
        # W Pythonie, używając 'self.i', również odnosimy się do atrybutu instancji.
        # Pętla 'for self.i in range(...)' będzie modyfikować atrybut 'self.i'
        # w każdej iteracji.
        for self.i in range(self.N): # range(self.N) da wartości od 0 do N-1
            print(f"z run {self.i}")
            # Małe opóźnienie, aby dać szansę przeplotu z wątkiem głównym
            # (opcjonalne, dla lepszej demonstracji współbieżności)
            time.sleep(0.001) 
        print("koniec run")

# Odpowiednik public static void main(String args[])
if __name__ == "__main__":
    # Tworzenie instancji wątku
    t = ThreadTester2()
    
    # Uruchomienie wątku (wywołuje metodę run() w nowym wątku)
    t.start()
    
    # Pętla w wątku głównym
    # W Pythonie zmienna pętli 'k' jest lokalna dla pętli
    for k in range(ThreadTester2.N): # Można też użyć t.N lub po prostu N, jeśli N jest globalne
        print(f"z main {k}")
        # Małe opóźnienie (opcjonalne)
        time.sleep(0.001)
    print("koniec main")

    # Opcjonalnie: poczekaj aż wątek 't' zakończy pracę.
    # W oryginalnym kodzie Javy tego nie ma, więc wątek główny może zakończyć się
    # przed lub w trakcie wykonywania wątku 't'.
    # Jeśli chcemy mieć pewność, że wątek 't' zakończy się przed wyjściem z programu:
    # t.join()
    # print("Wątek t zakończył pracę po join.")