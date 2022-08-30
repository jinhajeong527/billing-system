package com.example.demo.exception;

public class BillingSystemException extends Exception {

    public BillingSystemException(Throwable cause) {
        super(cause);
    }

    public BillingSystemException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
