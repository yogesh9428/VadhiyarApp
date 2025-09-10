package com.example.apnuvadhiyar

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apnuvadhiyar.screens.LoginScreen
import com.example.apnuvadhiyar.screens.home.HomeScreen
import com.example.apnuvadhiyar.screens.shop.ShopScreen
import com.example.apnuvadhiyar.screens.community.CommunityScreen
import com.example.apnuvadhiyar.util.TokenManager

class Navigation {
    sealed class Screen(val route: String, val title: String) {
        object Login : Screen("login", "Login")
        object Home : Screen("home", "Home")
        object Community : Screen("community", "Community")
        object Settings : Screen("settings", "Settings")
    }
}

// Main NavHost Composable
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Check if user is already logged in (token exists)
//    val token = remember { TokenManager.getToken(context) }
    var token by remember { mutableStateOf<String?>(null) }

    val startDestination = if (token.isNullOrEmpty()) {
        Navigation.Screen.Login.route
    } else {
        Navigation.Screen.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Navigation.Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                // Navigate to Home after successful login
                navController.navigate(Navigation.Screen.Home.route) {
                    popUpTo(Navigation.Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(Navigation.Screen.Home.route) {
            HomeScreen()
        }

        composable(Navigation.Screen.Community.route) {
            CommunityScreen()
        }

        composable(Navigation.Screen.Settings.route) {
            ShopScreen()
        }
    }
}
