package com.example.apnuvadhiyar.screens.community

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class CommunityProject(
    val emoji: String,
    val title: String,
    val description: String,
    var participants: Int,
    var joinedDate: String? = null
)

@SuppressLint("SimpleDateFormat")
@Composable
fun CommunityScreen() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val projects = remember {
        mutableStateListOf(
            CommunityProject("🌳", "Tree Planting Drive", "Join our mission to plant trees across the village!", 8),
            CommunityProject("🚮", "Clean Street Sunday", "Help us clean local streets this weekend!", 12),
            CommunityProject("📚", "Donate Books", "Contribute old books for village kids.", 5)
        )
    }

    val totalParticipants = projects.sumOf { it.participants }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Total joined badge
        Surface(
            shape = CircleShape,
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "🎉 Total Joined: $totalParticipants",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(projects.size) { index ->
                val project = projects[index]

                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFFE8F5E9),
                                        Color(0xFFFFFFFF)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "${project.emoji} ${project.title}",
                            fontSize = 20.sp,
                            color = Color(0xFF1B5E20),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = project.description,
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )

                        if (project.joinedDate != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "📅 Joined on: ${project.joinedDate}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF00C853),
                                modifier = Modifier
                            ) {
                                Text(
                                    text = "👥 ${project.participants} Joined",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    project.participants++
                                    val currentDate = SimpleDateFormat("dd MMM yyyy").format(Date())
                                    project.joinedDate = currentDate
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("🎉 Thanks for joining ${project.title}!")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                            ) {
                                Text("Join", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // SnackBar Host (at bottom of the screen)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
