package com.ecommerce.cart.aggregate.vo;

import com.ecommerce.cart.aggregate.exception.InvariantViolationException;
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

    public Quantity decrementQty(Quantity decrement, int scale) {
        int newQuantity = this.value - decrement.value;
        return new Quantity(newQuantity);
    }

    public static int findDelta(Quantity existingQuantity, Quantity newQuantity) {
        return newQuantity.value - existingQuantity.value;
    }

    public int compareTo(int anotherValue) {
        return this.value - anotherValue;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new InvariantViolationException("Shopping cart can have between 1 and 20 items");
        }
    }
}
