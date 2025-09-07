package com.vadhiyar.auth.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {
    private static final long EXPIRY_SECONDS = 120;
    private final Random random = new Random();
    private final Map<String , OtpRecord> store = new ConcurrentHashMap<>();

    public String generateOtp(String phone){
        String otp = String.format("%06d" , random.nextInt(1000000));
        store.put(phone ,new OtpRecord(otp , Instant.now().plusSeconds(EXPIRY_SECONDS)));
        return otp;
    }

    public boolean validateOtp(String phone , String otp){
        OtpRecord record = store.get(phone); // key - value pair
        if (record == null) return false;
        if (Instant.now().isAfter(record.expiry())) return false;
        return record.otp().equals(otp);
    }
    private record OtpRecord(String otp , Instant expiry) { }
}
