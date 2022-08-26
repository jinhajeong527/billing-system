package com.example.demo.exception.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.entity.ProductChangeHistory;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.OperationEnum;
import com.example.demo.model.ResultEnum;
import com.example.demo.repository.ProductChangeHistoryRepository;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> productNotFoundException(ProductNotFoundException e, HttpServletRequest request) {
        productChangeHistoryForException(e.getMessage(), request.getMethod());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriceInfoNotExistException.class)
    public ResponseEntity<?> priceInfoNotExistException(PriceInfoNotExistException e, HttpServletRequest request) {
        productChangeHistoryForException(e.getMessage(), request.getMethod());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> invalidFormatException(InvalidFormatException e, HttpServletRequest request) {
        String errorMessage = e.getMessage().substring(0, 200);
        productChangeHistoryForException(errorMessage, request.getMethod());
      
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private void productChangeHistoryForException(String errorMessage, String method) {
        ProductChangeHistory productChangeHistory = null;
        if("POST".equals(method)) {
            productChangeHistory = new ProductChangeHistory(errorMessage, ResultEnum.FAIL, OperationEnum.CREATE);
        } else if("PUT".equals(method)) {
            productChangeHistory = new ProductChangeHistory(errorMessage, ResultEnum.FAIL, OperationEnum.UPDATE);
        } else if("DELETE".equals(method)) {
            productChangeHistory = new ProductChangeHistory(errorMessage, ResultEnum.FAIL, OperationEnum.DELETE);
        }
        if(productChangeHistory !=  null) productChangeHistoryRepository.saveAndFlush(productChangeHistory);
    }
    
}
