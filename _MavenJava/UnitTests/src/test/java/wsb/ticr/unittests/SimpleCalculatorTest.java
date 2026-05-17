package wsb.ticr.unittests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCalculatorTest {

    SimpleCalculator sc = new SimpleCalculator();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.Test
    void add() {
        assertEquals(5.0,sc.add(2.0f, 3.0f),"dodawanie");
        assertEquals(2,sc.add(1,1));
    }

    @org.junit.jupiter.api.Test
    void subtract() {
        assertEquals(4,sc.subtract(8,4));
    }

    @org.junit.jupiter.api.Test
    void multiply() {
        assertEquals(7,sc.multiply(2,3.5f));
    }

    @org.junit.jupiter.api.Test
    void divide() {
        assertEquals(3.5,sc.divide(7,2));
        assertThrows(ArithmeticException.class,() -> {sc.divide(5,0);});
    }

    @Test
    @DisplayName("1 + 1 = 2")
    void addsTwoNumbers() {
        assertEquals(2, sc.add(1, 1), "1 + 1 should equal 2");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0,    1,   1",
            "1,    2,   3",
            "49,  51, 100",
            "1,  100, 101"
    })
    void add(int first, int second, int expectedResult) {
        assertEquals(expectedResult, sc.add(first, second),
                () -> first + " + " + second + " should equal " + expectedResult);
    }
}