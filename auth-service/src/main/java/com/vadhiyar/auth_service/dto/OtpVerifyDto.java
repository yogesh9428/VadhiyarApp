package com.vadhiyar.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpVerifyDto {
   @NotBlank(message = "Phone number is Required ")
   @Pattern(regexp = "\\d{10}" , message = "Phone number must be 10 digit ")
   private String phone;

   @NotBlank(message = "Otp is required")
   @Pattern(regexp = "\\d{6}" , message = "OTP must be 6 digit")
   private String otp;

   public String getPhone(){
       return this.phone;
   }
   public void setPhone(String phone){
       this.phone = phone;
   }

   public String getOtp(){
       return this.otp;
   }

   public void setOtp(String otp){
       this.otp = otp;
   }


}
