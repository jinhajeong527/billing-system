package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.BillingRequest;
import com.example.demo.dto.response.BillingResponsePayload;
import com.example.demo.exception.BillingSystemException;
import com.example.demo.service.BillingService;

@RestController
@RequestMapping(path = "api/billing")
public class BillingController {
    
    @Autowired
    BillingService billingservice;

    @PostMapping
    public ResponseEntity<?> getBillingInfo(@RequestBody BillingRequest billingRequest) throws BillingSystemException {
        BillingResponsePayload billingResponsePayload = billingservice.getBillingInfo(billingRequest);
        return new ResponseEntity<>(billingResponsePayload, HttpStatus.OK);
    }

    
    
}
