package com.example.apnuvadhiyar

class Navigation {
    sealed class Screen(val route: String, val title: String) {
        object Home : Screen("home", "Home")
        object Community : Screen("community", "Community")
        object Settings : Screen("settings", "Settings") // Example for another screen
    }
}