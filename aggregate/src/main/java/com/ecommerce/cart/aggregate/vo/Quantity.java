package com.ecommerce.cart.aggregate.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Quantity {

    @Getter
    private int value;
    private static final int MAX_QUANTITY = 20;

    public Quantity() {
        this(0);
    }

    public Quantity(int value) {
        validateQuantity(value);
        this.value = value;
    }

    public Quantity incrementQty(Quantity increment) {
        int newQuantity = this.value + increment.value;
        return new Quantity(newQuantity);
    }

    public Quantity decrementQty(Quantity decrement) {
        int newQuantity = this.value - decrement.value;
        return new Quantity(newQuantity);
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new IllegalStateException("Shopping cart can have between 0 and 20 items");
        }
    }
}
