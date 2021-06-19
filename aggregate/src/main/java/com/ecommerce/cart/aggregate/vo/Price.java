package com.ecommerce.cart.aggregate.vo;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal value;
    private static final BigDecimal MAX_PRICE = new BigDecimal(1000);

    public Price() {
        value = new BigDecimal(0);
    }

    private Price(BigDecimal value) {
        this.value = value;
    }

    public Price incrementPrice(BigDecimal increment) {
        BigDecimal newValue = this.value.add(increment);
        if (value.compareTo(MAX_PRICE) > 0)
            throw new IllegalStateException("Max price on the shopping cart is 1000, " +
                    "you're price is " + newValue);

        return new Price(newValue);
    }
}
