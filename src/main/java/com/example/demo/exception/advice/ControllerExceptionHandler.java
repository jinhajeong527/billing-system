package com.example.demo.exception.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.entity.ProductChangeHistory;
import com.example.demo.exception.PriceInfoNotExistException;
import com.example.demo.exception.ProductInfoNotExistException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.model.OperationEnum;
import com.example.demo.model.ResultEnum;
import com.example.demo.repository.ProductChangeHistoryRepository;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @Autowired
    ProductChangeHistoryRepository productChangeHistoryRepository;

    @ExceptionHandler({ProductInfoNotExistException.class, PriceInfoNotExistException.class, ProductNotFoundException.class})
    public ResponseEntity<?> priceInfoNotExistException(RuntimeException e, HttpServletRequest request) {
        productChangeHistoryForException(e.getMessage(), request.getMethod());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> otherException(Exception e, HttpServletRequest request) {
        int index = e.getMessage().length() > 100 ? 100 : e.getMessage().length();
        String errorMessage = e.getMessage().substring(0, index);
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
