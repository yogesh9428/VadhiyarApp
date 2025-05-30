package com.example.apnuvadhiyar.screens.shop

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.apnuvadhiyar.R



@Composable
fun ShopScreen() {
    val context = LocalContext.current

    val products = listOf(
        Product("Vadhiyar Oil 1L", "230", R.drawable.oil_dabbo),
        Product("Vadhiyar Oil 5L", "1150", R.drawable.dabbo_with_yello_bg),
        Product("Vadhiyar Oil 15L", "2650", R.drawable.dabbo_with_black_bg),
        Product("Vadhiyar Oil 15kg", "3000", R.drawable.dabbo_with_black_bg)
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF1F8E9), Color(0xFFE8F5E9))
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(products.size) { index ->
            val product = products[index]

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = product.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "₹${product.price}",
                            fontSize = 18.sp,
                            color = Color(0xFF616161),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val phone = "916355288823"
                                val upiLink = "upi://pay?pa=vishvas.jadav@ybl&pn=Apnu%20Vadhiyar&tn=${Uri.encode(product.name)}&am=&cu=INR"
                                val message = """
                                    Hello! I want to buy *${product.name}*.
                                    Price is ₹${product.price}.
                                    Is there any discount available for me?

                                    After confirmation, you can pay securely via this link:
                                    $upiLink

                                    Thank you!
                                """.trimIndent()
                                val url = "https://wa.me/$phone?text=${Uri.encode(message)}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                        ) {
                            Text("WhatsApp", color = Color.White)
                        }

                        Button(
                            onClick = {
                                val upiUri = Uri.parse("upi://pay").buildUpon()
                                    .appendQueryParameter("pa", "vishvas.jadav@ybl")
                                    .appendQueryParameter("pn", "Apnu Vadhiyar")
                                    .appendQueryParameter("tn", product.name)
                                    .appendQueryParameter("am", "")
                                    .appendQueryParameter("cu", "INR")
                                    .build()
                                val intent = Intent(Intent.ACTION_VIEW, upiUri)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Pay", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

data class Product(
    val name: String,
    val price: String,
    val imageRes: Int
)
