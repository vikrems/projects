package com.ecommerce.cart.aggregate.exception;

public class InvariantViolationException extends RuntimeException {

    public InvariantViolationException(String message) {
        super(message);
    }
}
