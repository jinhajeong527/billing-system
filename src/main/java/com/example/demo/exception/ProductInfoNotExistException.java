package com.example.demo.exception;

public class ProductInfoNotExistException extends RuntimeException {
    
    private static final long serialVersionUID = 98765432123L;

    public ProductInfoNotExistException(String message) {
        super(message);
    }
    public ProductInfoNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
