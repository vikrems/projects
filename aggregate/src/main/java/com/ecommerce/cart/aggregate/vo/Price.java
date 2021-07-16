package com.ecommerce.cart.aggregate.vo;

import com.ecommerce.cart.aggregate.exception.PriceExceededException;

import java.math.BigDecimal;

public class Price {

    private final BigDecimal value;
    private static final BigDecimal MAX_PRICE = new BigDecimal(300);

    public Price() {
        value = new BigDecimal(0);
    }

    private Price(BigDecimal value) {
        this.value = value;
    }

    public Price incrementPrice(BigDecimal increment, int scale) {
        BigDecimal totalIncrement = increment.multiply(new BigDecimal(scale));
        BigDecimal newValue = this.value.add(totalIncrement);
        if (newValue.compareTo(MAX_PRICE) > 0)
            throw new PriceExceededException("Max price on the shopping cart is 1000, " +
                    "you're price is " + newValue);

        return new Price(newValue);
    }

    public Price decrementPrice(BigDecimal decrement, int scale) {
        BigDecimal totalIncrement = decrement.multiply(new BigDecimal(scale));
        BigDecimal newValue = this.value.subtract(decrement.multiply(totalIncrement));

        return new Price(newValue);
    }

    public BigDecimal getValue(){
        return this.value;
    }
}
