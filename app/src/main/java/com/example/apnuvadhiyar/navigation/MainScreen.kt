package com.example.apnuvadhiyar.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.apnuvadhiyar.screens.LoginScreen
import com.example.apnuvadhiyar.screens.community.CommunityScreen
import com.example.apnuvadhiyar.screens.home.HomeScreen
import com.example.apnuvadhiyar.screens.shop.ShopScreen
import com.example.apnuvadhiyar.util.TokenManager

// --------------------
// Define Screens
// --------------------
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Shop : Screen("shop", "Shop", Icons.Filled.ShoppingCart)
    object Community : Screen("community", "Community", Icons.Filled.Person)
}

// --------------------
// Bottom Navigation Bar
// --------------------
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Home,
        Screen.Shop,
        Screen.Community
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                },
                label = { Text(screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) }
            )
        }
    }
}

// --------------------
// MainScreen Composable
// --------------------
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Read token dynamically
    var token by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        token = TokenManager.getToken(context)
    }

    // Decide start destination
    val startDestination = if (token.isNullOrEmpty()) "login" else Screen.Home.route

    Scaffold(
        bottomBar = {
            // Show bottom bar only for logged-in users
            if (!token.isNullOrEmpty()) BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // LoginScreen for users without token
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    // Save token and navigate to Home
                    token = TokenManager.getToken(context)
                    navController.navigate(Screen.Home.route) {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }

            // Main app screens
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Shop.route) { ShopScreen() }
            composable(Screen.Community.route) { CommunityScreen() }
        }
    }
}
