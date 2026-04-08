package wsb.merito.pz.cw03;
import java.util.concurrent.Executors;

public class VirtualThreads {
    public static void main(String[] args) {
        // Tworzymy egzekutor, który dla każdego zadania odpala lekki wirtualny wątek
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                try {
                    // 1. Pobranie użytkownika
                    String user = getUser();

                    // 2. Pobranie zamówień
                    String orders = getOrders(user);

                    // 3. Przetworzenie płatności
                    String status = processPayment(orders);

                    System.out.println("Finalny status: " + status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        // Executor zamknie się automatycznie po zakończeniu zadań
    }

    static String getUser() throws InterruptedException {
        Thread.sleep(1000); // W wirtualnym wątku to NIE blokuje systemu!
        System.out.println("1. Pobrano użytkownika");
        return "Jan Kowalski";
    }

    static String getOrders(String user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("2. Pobrano zamówienia dla: " + user);
        return "Monitor, Klawiatura";
    }

    static String processPayment(String orders) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("3. Przetworzono płatność za: " + orders);
        return "ZAKOŃCZONO SUKCESEM";
    }
}
