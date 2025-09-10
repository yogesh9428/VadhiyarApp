package com.vadhiyar.auth.controller;

import com.vadhiyar.auth.dto.OtpRequestDto;
import com.vadhiyar.auth.dto.OtpVerifyDto;
import com.vadhiyar.auth.model.User;
import com.vadhiyar.auth.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Value("${app.auth.devMode:true}")
    private boolean devMode;

    public AuthController(OtpService otpService , JwtService jwtService , UserRepository userRepository){
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.userRepository = userRepository;
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
//              return ResponseEntity.ok(Map.of(
//                      "status","OTP verified successfully" ,
//                      "Jwt-Token" , "dummy-Jwt-Token"
//              )

        if (!valid){
                return ResponseEntity.badRequest().body(Map.of(
                        "status " , "Otp verification failed" ,
                        "message" , "Invalid or expire otp"
                ));

          } else {

            User user = userRepository.findByPhone(verify.getPhone())
                    .orElseGet(()->{
                        User newUser = new User();
                        newUser.setPhone(verify.getPhone());
                        newUser.setRole("USER");
                        return userRepository.save(newUser);
                    });
              String jwtToken = jwtService.generateToken(verify.getPhone());
              return ResponseEntity.ok(Map.of(
                      "status" , "success" ,
                      "tokenType" , "Bearer" ,
                      "Jwt-Token" , jwtToken ,
                      "role" , user.getRole()
              ));
          }
    }
}
