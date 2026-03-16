package wsb.merito.pz.cw02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Examples {


    public static void main(String[] args) {

//Wprowadzenie do Stream API

        List<String> names = List.of("Anna", "Tomasz", "Kasia");

        names.forEach(System.out::println);

//Tworzenie strumieni

        Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2);

        infiniteStream.limit(5).forEach(System.out::println);


//Operacje pośrednie (Intermediate)

        List<String> words = List.of("java", "stream", "lambda", "java");
        words.stream()
                .filter(w -> w.length() > 4)
                .distinct()
                .sorted()
                .forEach(System.out::println);

//Operacje terminalne (Terminal)

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Suma: " + sum);

//Grupowanie i partycjonowanie danych

        names = List.of("Anna", "Tomasz", "Kasia", "Jan");
        Map<Integer, List<String>> groupedByLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println(groupedByLength);

//Strumienie równoległe (Parallel Streams)

        numbers = IntStream.range(1, 100).boxed().toList();
        sum = numbers.parallelStream().reduce(0, Integer::sum);
        System.out.println("Suma (równolegle): " + sum);

//Przetwarzanie plików i danych wejściowych

        System.out.println(Path.of("data.txt"));

        try (Stream<String> lines = Files.lines(Path.of("data.txt"))) {
            lines.filter(line -> line.contains("tempus"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}