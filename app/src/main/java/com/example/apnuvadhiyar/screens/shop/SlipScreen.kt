package com.example.apnuvadhiyar.screens.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SlipScreen(
    defaultSlipText: String,
    onSlipSend: (String) -> Unit,
    onBack: () -> Unit
) {
    var slipText by remember { mutableStateOf(defaultSlipText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Payment Confirmation Slip", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = slipText,
            onValueChange = { slipText = it },
            label = { Text("Slip Message") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            singleLine = false,
            maxLines = 10,
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { onBack() }, modifier = Modifier.weight(1f)) {
                Text("Back")
            }

            Button(onClick = { onSlipSend(slipText) }, modifier = Modifier.weight(1f)) {
                Text("Send Slip")
            }
        }
    }
}
