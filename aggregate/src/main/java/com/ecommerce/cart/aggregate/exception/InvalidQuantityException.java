package com.ecommerce.cart.aggregate.exception;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(String message) {
        super(message);
    }
}
