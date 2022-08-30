package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.LoginRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/auth")
public class AuthController {

    @PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        if(loginRequest.getUsername().equals("jinhajeong")
        && loginRequest.getPassword().equals("test")) {
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", "jinhajeong");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
}
