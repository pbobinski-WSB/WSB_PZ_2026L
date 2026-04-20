package wsb.merito.pz.cw04.nio_simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SimpleNioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel clientChannel = null;

        try {
            // 1. Otwórz kanał serwera
            serverSocketChannel = ServerSocketChannel.open();
            // 2. Powiąż z adresem i portem
            serverSocketChannel.bind(new InetSocketAddress("localhost", 9090));
            // serverSocketChannel.configureBlocking(true); // Domyślnie jest blokujący

            System.out.println("Serwer NIO nasłuchuje na porcie 9090 (tryb blokujący)...");

            // 3. Czekaj na połączenie klienta (blokujące)
            clientChannel = serverSocketChannel.accept(); // Ta linia blokuje do czasu połączenia klienta
            System.out.println("Klient połączony: " + clientChannel.getRemoteAddress());

            // 4. Przygotuj bufor
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 5. Odczytaj dane od klienta
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip(); // Przygotuj bufor do odczytu
                String message = StandardCharsets.UTF_8.decode(buffer).toString();
                System.out.println("Otrzymano od klienta: " + message.trim());

                // 6. Przygotuj odpowiedź (echo)
                buffer.clear(); // Wyczyść bufor do zapisu
                String response = "Serwer otrzymał: " + message.trim();
                buffer.put(response.getBytes(StandardCharsets.UTF_8));
                buffer.flip(); // Przygotuj bufor do zapisu do kanału

                // 7. Wyślij odpowiedź do klienta
                while (buffer.hasRemaining()) {
                    clientChannel.write(buffer);
                }
                System.out.println("Wysłano odpowiedź do klienta.");
            } else if (bytesRead == -1) {
                System.out.println("Klient zamknął połączenie.");
            }

        } catch (IOException e) {
            System.err.println("Błąd I/O serwera: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 8. Zamknij kanały
            if (clientChannel != null) {
                try {
                    clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Serwer zakończył działanie.");
        }
    }
}