## Tworzenie strumieni
'''
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
numbers.stream().forEach(System.out::println);
'''

numbers = [1, 2, 3, 4, 5]
for num in numbers:
    print(num)

## Operacje pośrednie
### Filtrowanie (`filter`)
'''
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
'''

evens = list(filter(lambda n: n % 2 == 0, numbers))
# Alternatywnie:
evens = [n for n in numbers if n % 2 == 0]

### Mapowanie (`map`)
'''
List<String> namesUpper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
'''

names = ["Anna", "Jan", "Piotr"]
names_upper = list(map(str.upper, names))
# Alternatywnie:
names_upper = [name.upper() for name in names]

### Płaskie mapowanie (`flatMap`)
'''
List<List<Integer>> lists = List.of(List.of(1, 2), List.of(3, 4));
List<Integer> flatList = lists.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
'''

lists = [[1, 2], [3, 4]]
flat_list = [n for sublist in lists for n in sublist]

## 3. Operacje końcowe
### 3.1 Iteracja (`forEach`)
'''
numbers.stream().forEach(System.out::println);
'''

for num in numbers:
    print(num)

### Kolekcjonowanie (`collect`)
'''
Set<Integer> numSet = numbers.stream()
    .collect(Collectors.toSet());
'''

num_set = set(numbers)

### Redukcja (`reduce`)
'''
int sum = numbers.stream()
    .reduce(0, Integer::sum);
'''

from functools import reduce
sum_numbers = reduce(lambda a, b: a + b, numbers, 0)
# Alternatywnie:
sum_numbers = sum(numbers)

## Grupowanie i partycjonowanie
### Grupowanie (`groupingBy`)
'''
Map<String, List<Person>> peopleByCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity));
'''

from itertools import groupby
people = [
    {"name": "Jan", "city": "Warszawa"},
    {"name": "Anna", "city": "Kraków"},
    {"name": "Piotr", "city": "Warszawa"}
]

people.sort(key=lambda p: p["city"])  # groupby wymaga posortowania
grouped = {k: list(v) for k, v in groupby(people, key=lambda p: p["city"])}

### Partycjonowanie (`partitioningBy`)
'''
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
'''

partitioned = {
    True: [n for n in numbers if n % 2 == 0],
    False: [n for n in numbers if n % 2 != 0]
}


## Strumienie równoległe (Parallel Streams)
'''
numbers.parallelStream()
    .map(n -> "Thread: " + Thread.currentThread().getName() + " processing " + n)
    .forEach(System.out::println);
'''

import multiprocessing

def process(n):
    return f"Process: {multiprocessing.current_process().name} processing {n}"

if __name__ == "__main__":  
    numbers = list(range(1, 11))  

    pool = multiprocessing.Pool(processes=multiprocessing.cpu_count())
    
    try:
        results = pool.map(process, numbers)  
        for r in results:
            print(r)
    finally:
        pool.close()  
        pool.join()   


## Operacje współbieżne
'''
AtomicInteger sum = new AtomicInteger(0);
numbers.parallelStream().forEach(n -> sum.addAndGet(n)); // ❌ Problem ze współbieżnością!
'''

import threading

sum_lock = threading.Lock()
sum_value = 0

def add(n):
    global sum_value
    with sum_lock:
        sum_value += n

threads = [threading.Thread(target=add, args=(n,)) for n in numbers]

if __name__ == "__main__":  

    for t in threads:
        t.start()
    for t in threads:
        t.join()

    print(sum_value)

## Przetwarzanie strumieniowe dużych zbiorów danych
'''
Files.lines(Paths.get("data.txt"))
    .map(String::toUpperCase)
    .forEach(System.out::println);
'''

with open("..\data.txt", "r") as file:
    for line in file:
        print(line.strip().upper())




