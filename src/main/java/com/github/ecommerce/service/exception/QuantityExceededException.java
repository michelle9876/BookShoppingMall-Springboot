package com.github.ecommerce.service.exception;

public class QuantityExceededException extends RuntimeException{
    public QuantityExceededException(String message) {
        super(message);
    }

}
