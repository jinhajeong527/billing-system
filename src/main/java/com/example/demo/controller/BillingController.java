package com.example.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BillingResponsePayload;
import com.example.demo.service.BillingService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/billing")
public class BillingController {
    
    @Autowired
    BillingService billingservice;

    @GetMapping
    public ResponseEntity<?> getBillingInfo() throws StreamReadException, DatabindException, MalformedURLException, IOException {
        int productId = 3;
        int targetYear = 2021;
        int targetMonth = 7;
        // 요금 계산하려는 month를 인자로 넣어준다.
        BillingResponsePayload billingResponsePayload = billingservice.getBillingInfo(productId, targetYear, targetMonth);
        return new ResponseEntity<>(billingResponsePayload, HttpStatus.OK);
    }

    
    
}
