package com.vadhiyar.auth.controller;

import com.vadhiyar.auth.dto.OtpRequestDto;
import com.vadhiyar.auth.dto.OtpVerifyDto;
import com.vadhiyar.auth.service.JwtService;
import com.vadhiyar.auth.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

//    @Autowired
//    private OtpService otpService;

    private final OtpService otpService;
    private final JwtService jwtService;

    @Value("${app.auth.devMode:true}")
    private boolean devMode;

    public AuthController(OtpService otpService , JwtService jwtService){
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Validated @RequestBody OtpRequestDto request) {
//        String otp = "123456";
        log.info("Otp request received for phone {}", request.getPhone());
        String otp = otpService.generateOtp(request.getPhone());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Phone", request.getPhone());
        body.put("message", "OTP generated");
        if (devMode) {
            body.put("otp ", otp); // tesing mode
            body.put("devMode", true);
        }
        return ResponseEntity.ok(body);
    }
//        return ResponseEntity.ok(Map.of(
//             "phone" , request.getPhone(),
//             "Otp" , otp ,
//             "Message" , "Otp generated successfully for testing purpose"
//        ));

        //public static <T> ResponseEntity<T> ok(T body) {
        //    return new ResponseEntity<>(body, HttpStatus.OK);
        //}
//    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody OtpVerifyDto verify){
        log.info("Otp verify attempt for phone {}" , verify.getPhone());
        boolean valid = otpService.validateOtp(verify.getPhone() , verify.getOtp());
//          if (verify.getOtp().equals("123456"))
          if (!valid){
//              return ResponseEntity.ok(Map.of(
//                      "status","OTP verified successfully" ,
//                      "Jwt-Token" , "dummy-Jwt-Token"
//              )
                return ResponseEntity.badRequest().body(Map.of(
                        "status " , "Otp verification failed" ,
                        "message" , "Invalid or expire otp"
                ));

          } else {
              String jwtToken = jwtService.generateToken(verify.getPhone());
              return ResponseEntity.ok(Map.of(
                      "status" , "success" ,
                      "tokenType" , "Bearer" ,
                      "Jwt-Token" , jwtToken ,
                      "expiryInMinutes" , 60
              ));
          }
    }
}
