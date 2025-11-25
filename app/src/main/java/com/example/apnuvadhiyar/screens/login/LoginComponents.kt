package com.example.apnuvadhiyar.screens.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhoneInputField(phone: String, onChange: (String) -> Unit) {
    TextField(
        value = phone,
        onValueChange = onChange,
        label = { Text("Phone Number") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OtpInputField(otp: String, onChange: (String) -> Unit) {
    TextField(
        value = otp,
        onValueChange = onChange,
        label = { Text("Enter OTP") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}
