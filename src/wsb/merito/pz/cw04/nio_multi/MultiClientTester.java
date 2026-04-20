package wsb.merito.pz.cw04.nio_multi;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger; // Dla bezpiecznego licznika klientów

public class MultiClientTester {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;
    private static final int NUM_CLIENTS = 10; // Liczba klientów do uruchomienia
    private static final int DELAY_MS = 100;    // Opóźnienie między startem kolejnych klientów (w ms)
    private static final AtomicInteger successfulConnections = new AtomicInteger(0);
    private static final AtomicInteger failedConnections = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.printf("Uruchamianie %d klientów testowych...\n", NUM_CLIENTS);

        Thread[] clientThreads = new Thread[NUM_CLIENTS];

        for (int i = 0; i < NUM_CLIENTS; i++) {
            final int clientId = i + 1; // Identyfikator klienta (zaczynając od 1)

            // Tworzymy Runnable dla logiki klienta
            Runnable clientTask = () -> {
                SocketChannel socketChannel = null;
                String threadName = Thread.currentThread().getName();
                try {
                    // 1. Otwórz kanał
                    socketChannel = SocketChannel.open();
                    // 2. Połącz z serwerem
                    socketChannel.connect(new InetSocketAddress(HOST, PORT));
                    // Czekaj na pełne połączenie (szczególnie ważne w trybie nieblokującym, ale tu dla pewności)
                    // W trybie blokującym connect() zwraca sukces lub rzuca wyjątek.
                    // Jeśli chcemy być bardziej NIO-like nawet w kliencie blokującym:
                    // socketChannel.configureBlocking(false);
                    // if (socketChannel.connect(new InetSocketAddress(HOST, PORT))) {
                    //    // Połączono od razu
                    // } else {
                    //    while (!socketChannel.finishConnect()) {
                    //        // Czekaj lub rób coś innego
                    //        TimeUnit.MILLISECONDS.sleep(10);
                    //    }
                    // }
                    // socketChannel.configureBlocking(true); // Można wrócić do blokującego po połączeniu

                    System.out.printf("[%s] Klient %d: Połączono z %s:%d\n", threadName, clientId, HOST, PORT);
                    successfulConnections.incrementAndGet();


                    // 3. Przygotuj i wyślij wiadomość
                    String message = "Wiadomość od klienta Java #" + clientId;
                    ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
                    System.out.printf("[%s] Klient %d: Wysyłanie: %s\n", threadName, clientId, message);
                    while (writeBuffer.hasRemaining()) {
                        socketChannel.write(writeBuffer);
                    }
                    // socketChannel.shutdownOutput(); // Opcjonalne: Sygnalizuje koniec wysyłania

                    // 4. Odczytaj odpowiedź
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int bytesRead = socketChannel.read(readBuffer);

                    if (bytesRead > 0) {
                        readBuffer.flip();
                        String response = StandardCharsets.UTF_8.decode(readBuffer).toString().trim();
                        System.out.printf("[%s] Klient %d: Otrzymano odpowiedź: %s\n", threadName, clientId, response);
                    } else if (bytesRead == -1) {
                        System.out.printf("[%s] Klient %d: Serwer zamknął połączenie przed odpowiedzią.\n", threadName, clientId);
                    } else {
                        System.out.printf("[%s] Klient %d: Nie otrzymano odpowiedzi (bytesRead=0).\n", threadName, clientId);
                    }

                } catch (ConnectException e) {
                    System.err.printf("[%s] Klient %d: BŁĄD - Nie można połączyć się z serwerem (%s:%d). Czy serwer działa?\n", threadName, clientId, HOST, PORT);
                    failedConnections.incrementAndGet();
                } catch (IOException e) {
                    System.err.printf("[%s] Klient %d: BŁĄD I/O: %s\n", threadName, clientId, e.getMessage());
                    failedConnections.incrementAndGet();
                } finally {
                    // 5. Zamknij kanał
                    if (socketChannel != null && socketChannel.isOpen()) {
                        try {
                            socketChannel.close();
                            System.out.printf("[%s] Klient %d: Połączenie zamknięte.\n", threadName, clientId);
                        } catch (IOException e) {
                            System.err.printf("[%s] Klient %d: Błąd podczas zamykania kanału: %s\n", threadName, clientId, e.getMessage());
                        }
                    }
                }
            }; // Koniec Runnable

            // Tworzymy i uruchamiamy wątek klienta
            clientThreads[i] = new Thread(clientTask, "KlientWątek-" + clientId);
            clientThreads[i].start();

            // Wprowadzamy małe opóźnienie, aby nie zalać serwera od razu
            try {
                TimeUnit.MILLISECONDS.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Główny wątek przerwany podczas opóźnienia.");
                break; // Przerwij pętlę tworzenia klientów
            }
        }

        System.out.println("\nWszystkie wątki klientów zostały uruchomione.");
        System.out.println("Oczekiwanie na zakończenie pracy klientów...");

        // Opcjonalnie: Czekaj na zakończenie wszystkich wątków klientów
        for (Thread t : clientThreads) {
            try {
                if (t != null) { // Sprawdź czy wątek został utworzony (na wypadek przerwania pętli)
                    t.join(); // Czekaj aż wątek zakończy działanie
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Główny wątek przerwany podczas oczekiwania na klientów.");
            }
        }

        System.out.println("\n--- Podsumowanie Testu ---");
        System.out.printf("Liczba uruchomionych klientów: %d\n", NUM_CLIENTS);
        System.out.printf("Udane połączenia: %d\n", successfulConnections.get());
        System.out.printf("Nieudane połączenia/Błędy: %d\n", failedConnections.get());
        System.out.println("--------------------------");
        System.out.println("Tester zakończył działanie.");
    }
}