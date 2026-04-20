package wsb.merito.pz.cw04.nio_simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimpleNioClient {

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // 1. Otwórz kanał klienta
            socketChannel = SocketChannel.open();
            // 2. Połącz z serwerem (operacja blokująca)
            socketChannel.connect(new InetSocketAddress("localhost", 9090));
            System.out.println("Połączono z serwerem: " + socketChannel.getRemoteAddress());

            // 3. Przygotuj bufor i wiadomość
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.print("Wpisz wiadomość do wysłania: ");
            String message = scanner.nextLine();

            // 4. Zapisz wiadomość do bufora i wyślij
            buffer.put(message.getBytes(StandardCharsets.UTF_8));
            buffer.flip(); // Przygotuj do zapisu do kanału
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            System.out.println("Wysłano: " + message);
            // socketChannel.shutdownOutput(); // Sygnalizuje koniec wysyłania (opcjonalne)

            // 5. Odczytaj odpowiedź serwera
            buffer.clear(); // Przygotuj bufor do odczytu z kanału
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip(); // Przygotuj bufor do odczytu danych
                String response = StandardCharsets.UTF_8.decode(buffer).toString();
                System.out.println("Otrzymano od serwera: " + response.trim());
            } else if (bytesRead == -1) {
                System.out.println("Serwer zamknął połączenie przed wysłaniem odpowiedzi.");
            }

        } catch (IOException e) {
            System.err.println("Błąd I/O klienta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Zamknij kanał
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            scanner.close();
            System.out.println("Klient zakończył działanie.");
        }
    }
}