package wsb.ticr.regresion;

import java.math.BigDecimal;

public class DiscountService {
    public BigDecimal calculateDiscount(BigDecimal price) {
        // UWAGA: BŁĄD LOGICZNY WPROWADZONY PODCZAS REFAKTORYZACJI!
        // Warunek powinien być "> 100", a jest ">= 100"
        if (price.compareTo(new BigDecimal("100")) > 0) {
            return price.multiply(new BigDecimal("0.10"));
        }
        return BigDecimal.ZERO;
    }
}