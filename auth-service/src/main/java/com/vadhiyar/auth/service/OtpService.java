package com.vadhiyar.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private static final long EXPIRY_SECONDS = 120;

    private final Random random = new Random();
    private final Map<String, OtpRecord> store = new ConcurrentHashMap<>();

    public String generateOtp(String phone) {
        String otp = String.format("%06d", random.nextInt(1000000));

        store.put(phone, new OtpRecord(otp, Instant.now().plusSeconds(EXPIRY_SECONDS)));

        log.info("OTP generated for phone {} (expires in {} sec): {}", phone, EXPIRY_SECONDS, otp);
        return otp;
    }

    public boolean validateOtp(String phone, String otp) {

        OtpRecord record = store.get(phone);

        if (record == null) {
            log.warn("OTP validation failed - no OTP found for phone {}", phone);
            return false;
        }

        if (Instant.now().isAfter(record.expiry())) {
            log.warn("OTP expired for phone {}", phone);
            store.remove(phone);
            return false;
        }

        boolean ok = record.otp().equals(otp);

        if (ok) {
            log.info("OTP validated successfully for phone {}", phone);
            store.remove(phone);
        } else {
            log.warn("Invalid OTP for phone {}", phone);
        }

        return ok;
    }

    private record OtpRecord(String otp, Instant expiry) {
    }
}
