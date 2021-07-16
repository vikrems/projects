package com.ecommerce.cart.aggregate.exception;

public class PriceExceededException extends RuntimeException {

    public PriceExceededException(String message) {
        super(message);
    }
}
