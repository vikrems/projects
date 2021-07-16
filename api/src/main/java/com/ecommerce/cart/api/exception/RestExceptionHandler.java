package com.ecommerce.cart.api.exception;

import com.ecommerce.cart.aggregate.exception.PriceExceededException;
import com.ecommerce.cart.aggregate.exception.QuantityExceededException;
import com.ecommerce.cart.service.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler({PriceExceededException.class, QuantityExceededException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }

//    @ExceptionHandler(QuantityExceededException.class)
//    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(QuantityExceededException ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
//        return new ResponseEntity<>(errorResponse, CONFLICT);
//    }
}
