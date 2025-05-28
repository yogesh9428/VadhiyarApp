package com.example.apnuvadhiyar.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apnuvadhiyar.R
@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val scrollState = rememberScrollState()
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .verticalScroll(scrollState)
            .padding(horizontal = 28.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeaderImage()

        Text(
            text = "Welcome to Apnu Vadhiyar",
            color = Color(0xFF003049),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Text(
            text = "Pure Oils | Fast Delivery | Community First",
            color = Color(0xFF37474F),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )

        Button(
            onClick = { navController.navigate("shop") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .height(52.dp)
                .width(220.dp)
                .padding(top = 6.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
        ) {
            Text(
                text = "Start Shopping",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🌟 Coming Soon 🌟",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004D40),
            letterSpacing = 0.8.sp
        )

        ComingSoonCard(
            icon = Icons.Filled.SolarPower,
            title = "Tata Solar Panel",
            description = "Launching Soon - Clean Energy for Your Home"
        )

        ComingSoonCard(
            icon = Icons.Filled.FormatPaint,
            title = "Tata Paint",
            description = "Coming Soon - Colors that Care"
        )
    }
}

@Composable
fun HomeHeaderImage() {
    val image: Painter = painterResource(id = R.drawable.full_detailed_page)

    var startAnimation by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.90f,
        animationSpec = tween(durationMillis = 2500)
    )
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f) // Image takes 60% of screen width
            .aspectRatio(220f / 320f)
            .clip(RoundedCornerShape(30.dp))
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(30.dp),
                ambientColor = Color(0x44000000),
                spotColor = Color(0x44000000)
            )
            .graphicsLayer {
                scaleX = scaleAnim
                scaleY = scaleAnim
            }
    ) {
        Image(
            painter = image,
            contentDescription = "Vadhiyar Oil Promo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp))
                .border(3.dp, Color(0xFF4CAF50), RoundedCornerShape(30.dp))
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0x88000000)),
                        startY = 120f
                    )
                )
                .clip(RoundedCornerShape(30.dp))
        )
    }
}

@Composable
fun ComingSoonCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Future click action or animation */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = elevatedCardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00796B),
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF004D40)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 15.sp,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}
