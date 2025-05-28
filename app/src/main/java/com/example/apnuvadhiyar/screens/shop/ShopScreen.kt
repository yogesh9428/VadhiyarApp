package com.example.apnuvadhiyar.screens.shop

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apnuvadhiyar.R

data class Product(
    val name: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun ShopScreen() {
    val context = LocalContext.current

    val products = listOf(
        Product("Vadhiyar Oil 1L", "230", R.drawable.oil_dabbo),
        Product("Vadhiyar Oil 5L", "1150", R.drawable.dabbo_with_yello_bg),
        Product("Vadhiyar Oil 15L", "2650", R.drawable.dabbo_with_black_bg),
        Product("Vadhiyar Oil 15kg", "3000", R.drawable.dabbo_with_black_bg)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(products.size) { index ->
            val product = products[index]

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = product.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "₹${product.price}",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WhatsApp")
                        }

                        Button(
                            onClick = {
                                val upiUri = Uri.parse("upi://pay").buildUpon()
                                    .appendQueryParameter("pa", "vishvas.jadav@ybl")
                                    .appendQueryParameter("pn", "Apnu Vadhiyar")
                                    .appendQueryParameter("tn", product.name)
                                    .appendQueryParameter("am", "") // Editable amount by user
                                    .appendQueryParameter("cu", "INR")
                                    .build()
                                val intent = Intent(Intent.ACTION_VIEW, upiUri)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Pay")
                        }
                    }
                }
            }
        }
    }
}
