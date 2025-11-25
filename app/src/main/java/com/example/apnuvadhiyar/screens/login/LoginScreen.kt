package com.example.apnuvadhiyar.screens.login

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Assuming these are defined in your project
import com.example.apnuvadhiyar.R
import com.example.apnuvadhiyar.data.*
import com.example.apnuvadhiyar.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*

// --- CONSTANTS & STYLES ---

val AccentColor = Color(0xFF007AFF)
val DarkBackground = Color(0xFF0A0A0A)
val HolographicLayerColor = Color.White.copy(alpha = 0.03f) // Even more subtle background

// Radial gradient to create a central spotlight/isolation effect
val HolographicRadialGradient = Brush.radialGradient(
    colors = listOf(Color(0xFF00B0FF).copy(alpha = 0.3f), DarkBackground.copy(alpha = 0.0f)),
    radius = 600f
)
val ResendCooldownSeconds = 30L // Constant for the timer

// --- LOGIN SCREEN COMPOSABLE ---

// Applied OptIn for the main AnimatedContent transition
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Resend Timer (Starts at 0, meaning active)
    var resendTimer by remember { mutableStateOf(0L) }

    // --- MAIN SCREEN LAYOUT ---

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground) // Darker base
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        // 💫 Holographic Radial Glow (Central Spotlight)
        Spacer(
            Modifier
                .size(400.dp)
                .background(HolographicRadialGradient)
                .align(Alignment.TopCenter)
                .offset(y = (-100).dp)
        )

        // 💫 The Aperture (The main login container)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp)) // Increased roundness
                // Layered background for depth (Holographic effect)
                .background(HolographicLayerColor)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        // Lighter, more futuristic border glow
                        listOf(Color.White.copy(alpha = 0.15f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                // Add a very subtle shadow (or outer glow) for lift
                .shadow(elevation = 16.dp, ambientColor = AccentColor.copy(alpha = 0.2f), shape = RoundedCornerShape(32.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title & Subtitle
            Text(
                text = if (!otpSent) "Secure Login" else "Confirm Code",
                fontSize = 36.sp, // Slightly larger
                fontWeight = FontWeight.Black, // Heavier weight
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = if (!otpSent)
                    "Enter your phone to proceed securely"
                else
                    "Code sent to +XX ${phone.substring(phone.length - 4)}", // Show last 4 digits only
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp)) // Increased spacing

            // --- Animated Content Transition ---

            AnimatedContent(
                targetState = otpSent,
                transitionSpec = {
                    slideInHorizontally(animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)) { if (targetState) it else -it } with
                            slideOutHorizontally(animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)) { if (targetState) -it else it }
                }
            ) { showOtp ->

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (!showOtp) {
                        // --- PHONE INPUT STAGE ---

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number", color = Color.White.copy(0.6f)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentColor,
                                unfocusedBorderColor = Color.White.copy(0.3f),
                                unfocusedLabelColor = Color.White.copy(0.6f),
                                focusedLabelColor = AccentColor,
                                cursorColor = AccentColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(30.dp))

                        GlowingCTAButton(
                            text = if (loading) "Sending Code..." else "Send OTP",
                            onClick = {
                                if (phone.length >= 10 && !loading) {
                                    loading = true
                                    errorMessage = null

                                    // 🚀 Call Network Logic (Original Callback)
                                    RetrofitClient.api.sendOtp(SendOtpRequest(phone))
                                        .enqueue(object : Callback<Void> {
                                            override fun onResponse(
                                                call: Call<Void>,
                                                response: Response<Void>
                                            ) {
                                                loading = false
                                                if (response.isSuccessful || response.code() == 429) {
                                                    // Handle 429 (Rate Limit) from backend
                                                    if (response.code() == 429) {
                                                        resendTimer = ResendCooldownSeconds
                                                        errorMessage = "Resend limit reached. Wait 30s."
                                                        otpSent = true
                                                    } else {
                                                        otpSent = true
                                                        resendTimer = ResendCooldownSeconds // Start timer on success
                                                    }
                                                } else {
                                                    errorMessage = "Failed to send OTP. Try again."
                                                }
                                            }

                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                loading = false
                                                errorMessage = "Network error: Check connection."
                                            }
                                        })
                                }
                            },
                            isLoading = loading
                        )

                    } else {
                        // --- OTP VERIFICATION STAGE ---

                        SixDigitOtpInput(
                            otpValue = otp,
                            onOtpChange = {
                                otp = it
                                if (it.length == 6) {
                                    coroutineScope.launch {
                                        delay(100)
                                        if (!loading) {
                                            loading = true
                                            verifyOtpCallback(phone, otp, onLoginSuccess, { errorMessage = it }, { loading = false })
                                        }
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.height(30.dp))

                        GlowingCTAButton(
                            text = if (loading) "Verifying..." else "Verify & Login",
                            onClick = {
                                if (otp.length == 6 && !loading) {
                                    loading = true
                                    errorMessage = null
                                    verifyOtpCallback(phone, otp, onLoginSuccess, { errorMessage = it }, { loading = false })
                                }
                            },
                            isLoading = loading,
                            enabled = otp.length == 6
                        )

                        // ⭐ ENHANCED RESEND LOGIC WITH TIMER ⭐
                        ResendCodeTimer(
                            timer = resendTimer,
                            onResendClicked = {
                                if (!loading) {
                                    loading = true
                                    errorMessage = null
                                    // Re-call the request OTP endpoint
                                    RetrofitClient.api.sendOtp(SendOtpRequest(phone))
                                        .enqueue(object : Callback<Void> {
                                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                                loading = false
                                                if (response.isSuccessful) {
                                                    resendTimer = ResendCooldownSeconds // Restart timer on success
                                                    errorMessage = "New OTP sent."
                                                } else {
                                                    errorMessage = "Error sending new OTP."
                                                }
                                            }
                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                loading = false
                                                errorMessage = "Network error during resend."
                                            }
                                        })
                                }
                            }
                        )

                        // Start the timer countdown only if it's currently running (i.e., > 0)
                        LaunchedEffect(resendTimer) {
                            if (resendTimer > 0) {
                                while (resendTimer > 0) {
                                    delay(1000L)
                                    resendTimer--
                                }
                            }
                        }
                    }
                }
            }

            // Error Message
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// --- NEW COMPOSABLE: RESEND BUTTON WITH TIMER ---

@OptIn(ExperimentalAnimationApi::class) // <-- FIX: OptIn applied here to resolve the latest warning
@Composable
fun ResendCodeTimer(timer: Long, onResendClicked: () -> Unit) {

    val isAvailable = timer == 0L

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        // Animated transition for the text change
        AnimatedContent(
            targetState = isAvailable,
            transitionSpec = {
                // Fade and scale for a smooth transition effect
                fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)) with
                        fadeOut(animationSpec = tween(150)) + scaleOut(animationSpec = tween(150))
            },
            label = "ResendButtonTransition"
        ) { available ->

            if (available) {
                // State 1: Resend Available
                TextButton(onClick = onResendClicked) {
                    Text(
                        "Resend Code",
                        color = AccentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                // State 2: Cooldown Active
                Text(
                    text = "Resend available in ${timer}s",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


// --- OTHER COMPOSABLES (REMAIN THE SAME) ---

@Composable
fun SixDigitOtpInput(otpValue: String, onOtpChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(otpValue))
    }

    LaunchedEffect(otpValue) {
        if (otpValue != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = otpValue)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(6) { index ->

            val char = otpValue.getOrElse(index) { ' ' }
            val isCurrentFocus = index == otpValue.length

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = when {
                            char != ' ' -> AccentColor
                            isCurrentFocus -> AccentColor.copy(alpha = 0.8f)
                            else -> Color.White.copy(alpha = 0.2f)
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color.White.copy(alpha = 0.05f))
                    .clickable { focusRequester.requestFocus() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val filteredValue = newValue.text.filter { it.isDigit() }.take(6)

            onOtpChange(filteredValue)
            textFieldValue = newValue.copy(
                text = filteredValue,
                selection = androidx.compose.ui.text.TextRange(filteredValue.length)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester)
            .wrapContentSize(align = Alignment.TopStart),
        decorationBox = {},
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@Composable
fun GlowingCTAButton(text: String, onClick: () -> Unit, isLoading: Boolean, enabled: Boolean = true) {
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00B0FF), AccentColor)
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(buttonGradient)
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(0.5f), AccentColor.copy(0.8f))
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Gray.copy(0.4f)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }
    }
}

// --- UTILITY FUNCTIONS FOR NETWORK LOGIC (REMAINS ORIGINAL) ---

fun verifyOtpCallback(
    phone: String,
    otp: String,
    onLoginSuccess: () -> Unit,
    onError: (String) -> Unit,
    onFinally: () -> Unit
) {
    RetrofitClient.api.verifyOtp(
        VerifyOtpRequest(phone, otp)
    ).enqueue(object : Callback<VerifyOtpResponse> {
        override fun onResponse(
            call: Call<VerifyOtpResponse>,
            response: Response<VerifyOtpResponse>
        ) {
            onFinally()
            if (response.isSuccessful && response.body() != null) {
                onLoginSuccess()
            } else {
                onError("Invalid OTP. Please check the code.")
            }
        }

        override fun onFailure(
            call: Call<VerifyOtpResponse>,
            t: Throwable
        ) {
            onFinally()
            onError("Verification failed. Network error.")
        }
    })
}