package wsb.ticr.unittests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testy dla klasy SimpleCalculator")
class SimpleCalculatorTestPretty {

    private SimpleCalculator calculator;

    // Ta metoda uruchomi się przed każdym testem, zapewniając nową, czystą instancję kalkulatora.
    @BeforeEach
    void setUp() {
        calculator = new SimpleCalculator();
    }

    @Nested
    @DisplayName("Operacja dodawania")
    class AdditionTests {

        @Test
        @DisplayName("powinno poprawnie dodawać dwie liczby dodatnie")
        void shouldAddTwoPositiveNumbers() {
            // Arrange
            float number1 = 5.0f;
            float number2 = 10.0f;

            // Act
            float result = calculator.add(number1, number2);

            // Assert
            assertEquals(15.0f, result, "5.0 + 10.0 powinno wynosić 15.0");
        }

        @Test
        @DisplayName("powinno poprawnie dodawać liczbę dodatnią i ujemną")
        void shouldAddPositiveAndNegativeNumber() {
            // Arrange
            float number1 = 10.0f;
            float number2 = -5.0f;

            // Act
            float result = calculator.add(number1, number2);

            // Assert
            assertEquals(5.0f, result, "10.0 + (-5.0) powinno wynosić 5.0");
        }

        @Test
        @DisplayName("dodawanie zera nie powinno zmieniać wartości")
        void shouldNotChangeValueWhenAddingZero() {
            // Arrange
            float number1 = 7.5f;
            float number2 = 0.0f;

            // Act
            float result = calculator.add(number1, number2);

            // Assert
            assertEquals(7.5f, result, "7.5 + 0.0 powinno wynosić 7.5");
        }
    }

    @Nested
    @DisplayName("Operacja odejmowania")
    class SubtractionTests {

        @Test
        @DisplayName("powinno poprawnie odejmować dwie liczby dodatnie")
        void shouldSubtractTwoPositiveNumbers() {
            // Arrange
            float number1 = 15.0f;
            float number2 = 3.0f;

            // Act
            float result = calculator.subtract(number1, number2);

            // Assert
            assertEquals(12.0f, result, "15.0 - 3.0 powinno wynosić 12.0");
        }

        @Test
        @DisplayName("wynik powinien być ujemny, gdy od mniejszej liczby odejmujemy większą")
        void shouldResultInNegativeWhenSubtractingLargerFromSmaller() {
            // Arrange
            float number1 = 5.0f;
            float number2 = 10.0f;

            // Act
            float result = calculator.subtract(number1, number2);

            // Assert
            assertEquals(-5.0f, result, "5.0 - 10.0 powinno wynosić -5.0");
        }
    }

    @Nested
    @DisplayName("Operacja mnożenia")
    class MultiplicationTests {

        @Test
        @DisplayName("powinno poprawnie mnożyć dwie liczby dodatnie")
        void shouldMultiplyTwoPositiveNumbers() {
            // Arrange
            float number1 = 6.0f;
            float number2 = 3.0f;

            // Act
            float result = calculator.multiply(number1, number2);

            // Assert
            assertEquals(18.0f, result, "6.0 * 3.0 powinno wynosić 18.0");
        }

        @Test
        @DisplayName("mnożenie przez zero powinno dawać w wyniku zero")
        void shouldResultInZeroWhenMultiplyingByZero() {
            // Arrange
            float number1 = 123.45f;
            float number2 = 0.0f;

            // Act
            float result = calculator.multiply(number1, number2);

            // Assert
            assertEquals(0.0f, result, "Każda liczba pomnożona przez 0 powinna dać 0");
        }
    }

    @Nested
    @DisplayName("Operacja dzielenia")
    class DivisionTests {

        @Test
        @DisplayName("powinno poprawnie dzielić dwie liczby dodatnie")
        void shouldDivideTwoPositiveNumbers() {
            // Arrange
            float number1 = 20.0f;
            float number2 = 4.0f;

            // Act
            float result = calculator.divide(number1, number2);

            // Assert
            assertEquals(5.0f, result, "20.0 / 4.0 powinno wynosić 5.0");
        }

        @Test
        @DisplayName("powinno rzucić ArithmeticException podczas próby dzielenia przez zero")
        void shouldThrowArithmeticExceptionWhenDividingByZero() {
            // Arrange
            float number1 = 10.0f;
            float number2 = 0.0f;

            // Act & Assert
            // Sprawdzamy, czy wykonanie operacji wewnątrz lambdy rzuci oczekiwany wyjątek.
            assertThrows(ArithmeticException.class, () -> {
                calculator.divide(number1, number2);
            }, "Dzielenie przez zero powinno rzucić ArithmeticException");
        }

        @Test
        @DisplayName("powinno poprawnie obsłużyć komunikat wyjątku przy dzieleniu przez zero")
        void shouldHaveCorrectExceptionMessageWhenDividingByZero() {
            // Arrange
            float number1 = 10.0f;
            float number2 = 0.0f;

            // Act
            // Przechwytujemy rzucony wyjątek, aby sprawdzić jego zawartość.
            ArithmeticException thrownException = assertThrows(ArithmeticException.class, () -> {
                calculator.divide(number1, number2);
            });

            // Assert
            assertEquals("Cannot divide by zero.", thrownException.getMessage());
        }
    }
}
