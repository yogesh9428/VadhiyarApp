package com.example.apnuvadhiyar.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.apnuvadhiyar.data.VerifyOtpResponse
import com.example.apnuvadhiyar.data.SendOtpRequest
import com.example.apnuvadhiyar.data.VerifyOtpRequest
import com.example.apnuvadhiyar.network.RetrofitClient
import com.example.apnuvadhiyar.util.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) { // <-- Added callback
    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!otpSent) {
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (phone.isNotEmpty()) {
                        loading = true
                        RetrofitClient.api.sendOtp(SendOtpRequest(phone))
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    loading = false
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "OTP sent!", Toast.LENGTH_SHORT).show()
                                        otpSent = true
                                    } else {
                                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    loading = false
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    } else {
                        Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !loading
            ) {
                Text(if (loading) "Sending..." else "Send OTP")
            }
        } else {
            TextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (otp.isNotEmpty()) {
                        loading = true
                        RetrofitClient.api.verifyOtp(VerifyOtpRequest(phone, otp))
                            .enqueue(object : Callback<VerifyOtpResponse> {
                                override fun onResponse(
                                    call: Call<VerifyOtpResponse>,
                                    response: Response<VerifyOtpResponse>
                                ) {
                                    loading = false
                                    if (response.isSuccessful && response.body() != null) {
                                        val token = response.body()!!.token
                                        TokenManager.saveToken(context, token)
                                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess() // <-- Trigger navigation to Home
                                    } else {
                                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {
                                    loading = false
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    } else {
                        Toast.makeText(context, "Enter OTP", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !loading
            ) {
                Text(if (loading) "Verifying..." else "Verify OTP")
            }
        }
    }
}
