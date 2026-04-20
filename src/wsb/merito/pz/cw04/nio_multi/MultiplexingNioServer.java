package wsb.merito.pz.cw04.nio_multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class MultiplexingNioServer {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    public static void main(String[] args) {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            // 1. Utwórz Selektor
            selector = Selector.open();

            // 2. Utwórz kanał serwera, ustaw nieblokujący i powiąż z adresem
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // Kluczowe dla NIO!
            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));

            // 3. Zarejestruj kanał serwera w selektorze dla operacji ACCEPT
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Serwer NIO (multiplexing) nasłuchuje na porcie " + PORT + "...");

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 4. Pętla główna serwera
            while (true) {
                // Czekaj na zdarzenia (blokuje do czasu wystąpienia zdarzenia)
                int readyChannels = selector.select();

                if (readyChannels == 0) {
                    continue; // Nic się nie wydarzyło, pętla dalej
                }

                // Pobierz zestaw kluczy gotowych do operacji
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    try {
                        // 5. Obsługa nowego połączenia
                        if (key.isAcceptable()) {
                            handleAccept(key, selector);
                        }
                        // 6. Obsługa odczytu danych od klienta
                        else if (key.isReadable()) {
                            handleRead(key, buffer);
                        }
                        // Można też obsłużyć key.isWritable() dla bardziej złożonych scenariuszy zapisu
                    } catch (IOException e) {
                        System.err.println("Błąd obsługi klienta: " + e.getMessage());
                        // W przypadku błędu (np. nagłego rozłączenia), zamknij kanał i usuń klucz
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException closeEx) {
                            // Ignoruj błąd zamykania
                        }
                    }

                    // 7. Usuń obsłużony klucz z zestawu
                    keyIterator.remove();
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd serwera NIO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Zamknij zasoby
            if (selector != null) {
                try {
                    selector.close();
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
            System.out.println("Serwer NIO zakończył działanie.");
        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept(); // Nie blokuje, bo wiemy, że jest połączenie
        clientChannel.configureBlocking(false); // Ważne! Kanał klienta też musi być nieblokujący
        clientChannel.register(selector, SelectionKey.OP_READ); // Zarejestruj do odczytu
        System.out.println("Zaakceptowano nowe połączenie od: " + clientChannel.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        buffer.clear(); // Przygotuj bufor do odczytu

        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            // Klient zamknął połączenie
            System.out.println("Klient " + clientChannel.getRemoteAddress() + " zamknął połączenie.");
            key.cancel(); // Usuń klucz z selektora
            clientChannel.close(); // Zamknij kanał
            return;
        }

        if (bytesRead > 0) {
            buffer.flip(); // Przygotuj bufor do odczytania danych
            String message = StandardCharsets.UTF_8.decode(buffer).toString().trim();
            System.out.println("Otrzymano od " + clientChannel.getRemoteAddress() + ": " + message);

            // Proste echo - przygotuj odpowiedź i wyślij z powrotem
            String response = "Echo: " + message;
            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
            while (responseBuffer.hasRemaining()) {
                // Uwaga: W bardziej złożonych aplikacjach, jeśli write() zwróci 0,
                // trzeba by zarejestrować OP_WRITE i dokończyć zapis później.
                // Tutaj zakładamy, że małe wiadomości zmieszczą się od razu.
                clientChannel.write(responseBuffer);
            }
            System.out.println("Wysłano echo do " + clientChannel.getRemoteAddress());
        }
    }
}