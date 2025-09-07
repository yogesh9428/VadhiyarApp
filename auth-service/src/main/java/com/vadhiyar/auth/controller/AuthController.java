package com.vadhiyar.auth.controller;

import com.vadhiyar.auth.dto.OtpRequestDto;
import com.vadhiyar.auth.dto.OtpVerifyDto;
import com.vadhiyar.auth.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Autowired
//    private OtpService otpService;

    private final OtpService otpService;
    public AuthController(OtpService otpService){
        this.otpService = otpService;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Validated @RequestBody OtpRequestDto request){
//        String otp = "123456";
        String otp = otpService.generateOtp(request.getPhone());
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
        boolean valid = otpService.validateOtp(verify.getPhone() , verify.getOtp());
//          if (verify.getOtp().equals("123456"))
          if (valid){
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
