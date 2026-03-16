# Wyrażenie lambda jako funkcja anonimowa

'''
import java.util.function.Function;

public class LambdaExample {
    public static void main(String[] args) {
        Function<Integer, Integer> square = x -> x * x;
        System.out.println(square.apply(5)); // 25
    }
}
'''

square = lambda x: x * x
print(square(5))  # 25

# Lambda w `forEach`

'''
import java.util.List;

public class LambdaForEach {
    public static void main(String[] args) {
        List<String> names = List.of("Anna", "Jan", "Piotr");
        names.forEach(name -> System.out.println("Cześć, " + name));
    }
}
'''

names = ["Anna", "Jan", "Piotr"]
list(map(lambda name: print(f"Cześć, {name}"), names))

# lub

for name in names:
    print(f"Cześć, {name}")

# Filtrowanie listy
'''
import java.util.List;
import java.util.stream.Collectors;

public class LambdaFilter {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println(evens); // [2, 4, 6]
    }
}
'''

numbers = [1, 2, 3, 4, 5, 6]
evens = list(filter(lambda n: n % 2 == 0, numbers))
print(evens)  # [2, 4, 6]

# lub

evens = [n for n in numbers if n % 2 == 0]
print(evens)  # [2, 4, 6]

# Sortowanie listy
'''
import java.util.List;
import java.util.Comparator;

public class LambdaSort {
    public static void main(String[] args) {
        List<String> names = List.of("Anna", "Jan", "Piotr");
        names.stream()
            .sorted((a, b) -> b.compareTo(a)) // Sortowanie odwrotne
            .forEach(System.out::println);
    }
}
'''

names = ["Anna", "Jan", "Piotr"]
names_sorted = sorted(names, key=lambda x: x, reverse=True)
print(names_sorted)  # ['Piotr', 'Jan', 'Anna']

# Mapowanie listy (`map`)
'''
import java.util.List;
import java.util.stream.Collectors;

public class LambdaMap {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4);
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println(squares); // [1, 4, 9, 16]
    }
}
'''

numbers = [1, 2, 3, 4]
squares = list(map(lambda n: n * n, numbers))
print(squares)  # [1, 4, 9, 16]

# lub

squares = [n * n for n in numbers]

# Redukcja (`reduce`)
'''
import java.util.List;
import java.util.Optional;

public class LambdaReduce {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4);
        Optional<Integer> sum = numbers.stream()
            .reduce((a, b) -> a + b);
        System.out.println(sum.orElse(0)); // 10
    }
}
'''

from functools import reduce

numbers = [1, 2, 3, 4]
sum_numbers = reduce(lambda a, b: a + b, numbers)
print(sum_numbers)  # 10

#  Wykorzystanie lambdy jako argument funkcji
'''
import java.util.function.Predicate;

public class LambdaAsArgument {
    public static boolean testNumber(int n, Predicate<Integer> predicate) {
        return predicate.test(n);
    }

    public static void main(String[] args) {
        boolean result = testNumber(5, n -> n > 0);
        System.out.println(result); // true
    }
}
'''

def test_number(n, predicate):
    return predicate(n)

result = test_number(5, lambda n: n > 0)
print(result)  # True

##  Przykład z klasą i wyrażeniami lambda
'''
import java.util.function.Supplier;

class Person {
    String name;
    Person(String name) { this.name = name; }
}

public class LambdaConstructor {
    public static void main(String[] args) {
        Supplier<Person> personFactory = () -> new Person("Jan");
        Person p = personFactory.get();
        System.out.println(p.name); // Jan
    }
}
'''

class Person:
    def __init__(self, name):
        self.name = name

person_factory = lambda: Person("Jan")
p = person_factory()
print(p.name)  # Jan

