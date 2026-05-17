package wsb.ticr.regresion;

import java.math.BigDecimal;

public class OrderProcessor {

    private final DiscountService discountService = new DiscountService();



    // Prosta logika: rabat 10% dla zamówień powyżej 100
    public BigDecimal calculateTotal(BigDecimal basePrice) {
    /*/
        if (basePrice.compareTo(new BigDecimal("100")) > 0) {
            BigDecimal discount = basePrice.multiply(new BigDecimal("0.10"));
            return basePrice.subtract(discount);
        }
        return basePrice;
    /*/
        BigDecimal discount = discountService.calculateDiscount(basePrice);
        return basePrice.subtract(discount);

    /**/
    }
}
