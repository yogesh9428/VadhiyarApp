package com.vadhiyar.auth_service.controller;

import com.vadhiyar.auth_service.dto.OtpRequestDto;
import com.vadhiyar.auth_service.dto.OtpVerifyDto;
import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Validated @RequestBody OtpRequestDto request){
        String otp = "123456";
        return ResponseEntity.ok(Map.of(
             "phone" , request.getPhone(),
             "Otp" , otp ,
             "Message" , "Otp generated successfully for testing purpose"
        ));

        //public static <T> ResponseEntity<T> ok(T body) {
        //    return new ResponseEntity<>(body, HttpStatus.OK);
        //}
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody OtpVerifyDto verify){
          if (verify.getOtp().equals("123456")){
              return ResponseEntity.ok(Map.of(
                      "status","OTP verified successfully" ,
                      "Jwt-Token" , "dummy-Jwt-Token"
              ));
          } else {
              return ResponseEntity.badRequest().body(Map.of(
                      "status" , "failed" ,
                      "Jwt-Token" , "Token generation failed"
              ));
          }
    }
}
