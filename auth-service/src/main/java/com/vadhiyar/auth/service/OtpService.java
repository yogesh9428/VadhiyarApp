package com.vadhiyar.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {

    private static final Logger log  = LoggerFactory.getLogger(OtpService.class);
    private static final long EXPIRY_SECONDS = 120;
    private final Random random = new Random();
    private final Map<String , OtpRecord> store = new ConcurrentHashMap<>();

    public String generateOtp(String phone){
        String otp = String.format("%06d" , random.nextInt(1000000));
        store.put(phone ,new OtpRecord(otp , Instant.now().plusSeconds(EXPIRY_SECONDS)));
        log.info("Generated otp for phone {} which is going to expire in {} second and otp is {}" , phone , EXPIRY_SECONDS , otp);
        return otp;
    }

    public boolean validateOtp(String phone , String otp){
        OtpRecord record = store.get(phone); // key - value pair
        if (record == null) {
            log.warn("Otp validation is failed : no otp for phone {}" , phone);
            return false;
        }
        if (Instant.now().isAfter(record.expiry())) {
            log.warn("Otp validation is failed : expired for phone {}" , phone);
            store.remove(phone); // cleaning up
            return false;
        }
        boolean ok = record.otp().equals(otp);
        if (ok){
            log.info("Otp validation is done successfully for phone {}" , phone);
            store.remove(phone);
        } else {
            log.warn("otp validation failed : the otp you enter is invalid for phone {}" , phone);
        }
        return ok;
    }
    private record OtpRecord(String otp , Instant expiry) { }
}
