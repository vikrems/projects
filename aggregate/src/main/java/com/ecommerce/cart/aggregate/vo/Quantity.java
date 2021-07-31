package com.ecommerce.cart.aggregate.vo;

import com.ecommerce.cart.aggregate.exception.InvalidQuantityException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class Quantity {

    @Getter
    private int value;
    private static final int MAX_QUANTITY = 20;

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

    public int compareTo(Quantity anotherValue) {
        return this.value - anotherValue.getValue();
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new InvalidQuantityException("Shopping cart can have between 1 and 20 items. " +
                    "Your quantity is " + quantity);
        }
    }
}
