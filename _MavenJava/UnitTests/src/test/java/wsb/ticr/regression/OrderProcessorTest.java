package wsb.ticr.regression;

import org.junit.jupiter.api.Test;
import wsb.ticr.regresion.OrderProcessor;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderProcessorTest {
    private final OrderProcessor processor = new OrderProcessor();

    @Test
    void testNoDiscountForSmallOrder() {
        // Zamówienie za 50, bez rabatu
        assertEquals(0, new BigDecimal("50.00").compareTo(processor.calculateTotal(new BigDecimal("50.00"))));
    }

    @Test
    void testBoundaryConditionForDiscount() {
        // Zamówienie za 100, jeszcze bez rabatu
        assertEquals(0, new BigDecimal("100.00").compareTo(processor.calculateTotal(new BigDecimal("100.00"))));
    }

    @Test
    void testDiscountForLargeOrder() {
        // Zamówienie za 200, oczekiwany rabat 10% (20), cena końcowa 180
        assertEquals(0, new BigDecimal("180.00").compareTo(processor.calculateTotal(new BigDecimal("200.00"))));
    }
}