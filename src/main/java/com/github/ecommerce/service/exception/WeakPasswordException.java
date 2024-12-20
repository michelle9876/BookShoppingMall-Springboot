package com.github.ecommerce.service.exception;

public class WeakPasswordException extends RuntimeException{
    public WeakPasswordException(String message) {
        super(message);
    }

}
