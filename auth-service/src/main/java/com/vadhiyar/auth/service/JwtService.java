package com.vadhiyar.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
//import java.time.temporal.ChronoUnit;


@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final SecretKey key;
    private final long expiryMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret ,
            @Value("${jwt.expiryMinutes:60}") long expiryMinutes)
    {
       this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); //HS256
       this.expiryMinutes = expiryMinutes;
    }

    public SecretKey getKey(){
        return key;
    }

    public String generateToken(String subjectPhone){
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(subjectPhone)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiryMinutes * 60)))
                .signWith(key) // no algorithm needed—it's inferred
                .compact();
        log.info("JWT token is issued for phone " , subjectPhone);
        log.info(" JWT GENERATED: {}", token);
        return token;
    }
}
