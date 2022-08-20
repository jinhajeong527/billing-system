package com.example.demo.exception;

public class ProductNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 98765432123L;

    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
