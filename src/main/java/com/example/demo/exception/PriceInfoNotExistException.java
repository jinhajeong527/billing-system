package com.example.demo.exception;

public class PriceInfoNotExistException extends RuntimeException {
    
    private static final long serialVersionUID = 98765432123L;

    public PriceInfoNotExistException(String message) {
        super(message);
    }
    public PriceInfoNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
