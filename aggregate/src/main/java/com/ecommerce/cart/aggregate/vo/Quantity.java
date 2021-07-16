package com.ecommerce.cart.aggregate.vo;

import com.ecommerce.cart.aggregate.exception.QuantityExceededException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Quantity {

    @Getter
    private int value;
    private static final int MAX_QUANTITY = 20;

    public Quantity() {
    }

    public Quantity(int value) {
        validateQuantity(value);
        this.value = value;
    }

    public Quantity incrementQty(Quantity increment) {
        int newQuantity = this.value + increment.value;
        return new Quantity(newQuantity);
    }

    public int compareTo(int anotherValue) {
        return this.value - anotherValue;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new QuantityExceededException("Shopping cart can have between 1 and 20 items");
        }
    }
}
