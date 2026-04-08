package wsb.merito.pz.cw03;

import java.util.concurrent.CompletableFuture;
import java.util.List;

// Klasa pomocnicza
class User {
    int id; String name;
    User(int id, String name) { this.id = id; this.name = name; }
}

public class AsyncDemo {

    // Zwraca odpowiednik Promise (CompletableFuture)
    static CompletableFuture<User> getUser(int id) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Rozpoczynam pobieranie użytkownika...");
            sleep(1000); // Symulacja opóźnienia I/O
            return new User(id, "Jan Kowalski");
        });
    }

    static CompletableFuture<List<String>> getOrders(User user) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Szukam zamówień dla: " + user.name + "...");
            sleep(1000);
            return List.of("Laptop", "Myszka");
        });
    }

    static CompletableFuture<String> processPayment(List<String> orders) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Przetwarzam płatność za " + orders.size() + " przedmioty...");
            sleep(1000);
            return "Płatność zakończona sukcesem!";
        });
    }

    public static void main(String[] args) {
        // Łańcuch obietnic - dokładnie jak .then() w JavaScript!
        CompletableFuture<Void> process = getUser(1)
                .thenCompose(user -> getOrders(user))
                .thenCompose(orders -> processPayment(orders))
                .thenAccept(result -> {
                    System.out.println("\n--- WYNIK ---");
                    System.out.println("Koniec procesu (Java CompletableFuture): " + result);
                });

        // W Javie główny wątek (main) by się zakończył od razu.
        // .join() każe mu poczekać na zakończenie asynchronicznych operacji z tła.
        process.join();
    }

    // Pomocnicza metoda do usypiania z ukrytym try-catch
    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }
}