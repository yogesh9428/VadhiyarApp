package com.vadhiyar.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @GetMapping("/user/me")
    public ResponseEntity<?> user(Authentication authentication){
        return ResponseEntity.ok(Map.of(
            "Phone" , authentication.getName(),
                "message" , "This is protected endpoint"
        ));
    }
}
