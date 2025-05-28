package com.example.apnuvadhiyar.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apnuvadhiyar.R

@Composable
fun HomeHeaderImage() {
    val image: Painter = painterResource(id = R.drawable.full_detailed_page) // Replace with your renamed image

    Image(
        painter = image,
        contentDescription = "Vadhiyar Oil Promo",
        modifier = Modifier
            .width(220.dp)
            .height(320.dp) // Covers ~40% of screen height
            .padding(top = 20.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .border(
                width = 2.dp,
                color = Color(0xFF4CAF50),
                shape = RoundedCornerShape(24.dp)
            )
    )
}

@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeaderImage()

        Text(
            text = "Welcome to Apnu Vadhiyar",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF01579B)
        )

        Text(
            text = "Pure Oils | Fast Delivery | Community First",
            fontSize = 16.sp,
            color = Color(0xFF0277BD)
        )

        Button(
            onClick = { navController.navigate("shop") },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Start Shopping", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🌟 Coming Soon 🌟",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF006064)
        )

        ComingSoonCard("Tata Solar Panel", "Launching Soon - Clean Energy for Your Home")
        ComingSoonCard("Tata Paint", "Coming Soon - Colors that Care")
    }
}

@Composable
fun ComingSoonCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00695C))
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
