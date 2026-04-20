package wsb.merito.pz.cw04.nio_tutorial.buffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class BufferLifecycleExample {

    public static void main(String[] args) {
        Path filePath = Path.of("data.txt"); // Załóżmy, że ten plik istnieje

        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            // 1. Przygotowanie bufora
            ByteBuffer buffer = ByteBuffer.allocate(1024); // Tworzymy bufor o pojemności 1024 bajtów
            System.out.println("--- Przygotowanie bufora ---");
            printBufferDetails(buffer);

            // 2. Wypełnienie bufora danymi (odczyt z kanału)
            int bytesRead = fileChannel.read(buffer); // Odczytuje dane z kanału do bufora
            if (bytesRead > 0) {
                System.out.println("\n--- Wypełnienie bufora danymi (" + bytesRead + " bajtów odczytano) ---");
                printBufferDetails(buffer);

                // 3. Metoda flip() - przygotowanie do odczytu z bufora
                buffer.flip();
                System.out.println("\n--- Wywołanie metody flip() ---");
                printBufferDetails(buffer);

                // 4. Opróżnianie bufora (odczyt danych z bufora)
                System.out.println("\n--- Opróżnianie bufora ---");
                while (buffer.hasRemaining()) {
                    byte b = buffer.get(); // Pobiera jeden bajt z bufora
                    System.out.print((char) b); // Interpretuje bajt jako znak (uproszczenie)
                }
                System.out.println();
                printBufferDetails(buffer);

                // Przygotowanie bufora do ponownego wypełnienia (opcjonalne)
                buffer.clear(); // Lub buffer.compact();
                System.out.println("\n--- Przygotowanie bufora do ponownego użycia (clear()) ---");
                printBufferDetails(buffer);
            } else if (bytesRead == -1) {
                System.out.println("Koniec strumienia.");
            } else {
                System.out.println("Nie odczytano żadnych danych.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBufferDetails(ByteBuffer buffer) {
        System.out.println("Pozycja: " + buffer.position());
        System.out.println("Limit:    " + buffer.limit());
        System.out.println("Pojemność: " + buffer.capacity());
    }
}
