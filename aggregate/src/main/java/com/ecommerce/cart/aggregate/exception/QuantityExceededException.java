package com.ecommerce.cart.aggregate.exception;

public class QuantityExceededException extends RuntimeException {

    public QuantityExceededException(String message) {
        super(message);
    }
}
