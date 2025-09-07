package com.vadhiyar.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpRequestDto {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}" , message = "Phone number must be 10 digit")
    private String phone;

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
