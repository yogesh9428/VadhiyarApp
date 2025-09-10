package com.example.apnuvadhiyar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.apnuvadhiyar.navigation.MainScreen
import com.example.apnuvadhiyar.ui.theme.ApnuVadhiyarTheme
import com.example.apnuvadhiyar.util.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.clearToken(this)

        enableEdgeToEdge()
        setContent {
            ApnuVadhiyarTheme {
                MainScreen() // ← Full app with navigation
            }
        }
    }
}
