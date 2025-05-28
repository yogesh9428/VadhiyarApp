package com.example.apnuvadhiyar.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.apnuvadhiyar.screens.community.CommunityScreen
import com.example.apnuvadhiyar.screens.home.HomeScreen
import com.example.apnuvadhiyar.screens.shop.ShopScreen

// Define Screens
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Shop : Screen("shop", "Shop", Icons.Filled.ShoppingCart)
    object Community : Screen("community", "Community", Icons.Filled.Person)
}

// Bottom Navigation Bar
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
                    // Ensures no multiple copies of the same screen in the back stack
                    navController.navigate(screen.route) {
                        // Pop up to the start of the graph to avoid back stack issues
                        launchSingleTop = true
                    }
                },
                label = { Text(screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) }
            )
        }
    }
}

// Main Screen
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Shop.route) { ShopScreen() }
            composable(Screen.Community.route) { CommunityScreen() }
        }
    }
}
