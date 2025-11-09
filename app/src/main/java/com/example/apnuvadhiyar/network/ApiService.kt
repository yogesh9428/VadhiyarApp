package com.example.apnuvadhiyar.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

import com.example.apnuvadhiyar.data.SendOtpRequest
import com.example.apnuvadhiyar.data.VerifyOtpRequest
import com.example.apnuvadhiyar.data.VerifyOtpResponse


interface ApiService {
    @POST("/auth/request-otp")
    fun sendOtp(@Body request: SendOtpRequest): Call<Void>

    @POST("/auth/verify-otp")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>
}
