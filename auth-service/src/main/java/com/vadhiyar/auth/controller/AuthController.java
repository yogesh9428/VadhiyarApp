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

    private final OtpService otpService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${app.auth.devMode:true}")
    private boolean devMode;

    public AuthController(OtpService otpService, JwtService jwtService, UserRepository userRepository) {
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@Validated @RequestBody OtpRequestDto request) {

        log.info("OTP request received for phone {}", request.getPhone());
        String otp = otpService.generateOtp(request.getPhone());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("phone", request.getPhone());
        body.put("message", "OTP generated");

        if (devMode) {
            body.put("otp", otp);     // testing only
            body.put("devMode", true);
        }
        return ResponseEntity.ok(body);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody OtpVerifyDto verify) {

        log.info("OTP verify attempt for phone {}", verify.getPhone());

        // Dev mode shortcut
        if (devMode && verify.getOtp().equals("123456")) {
            return ResponseEntity.ok(Map.of(
                    "status", "OTP verified (dev mode)",
                    "Jwt-Token", "dummy-JWT-token",
                    "tokenType", "Bearer",
                    "role", "USER"
            ));
        }

        boolean valid = otpService.validateOtp(verify.getPhone(), verify.getOtp());

        if (!valid) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Invalid or expired OTP",
                    "Jwt-Token", "",
                    "tokenType", "",
                    "role", ""
            ));
        }

        // Create or fetch user
        User user = userRepository.findByPhone(verify.getPhone())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setPhone(verify.getPhone());
                    newUser.setRole("USER");
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(verify.getPhone());

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "tokenType", "Bearer",
                "Jwt-Token", token,
                "role", user.getRole()
        ));
    }
}
