package com.example.dealspy.view.navigation

sealed class DealSpyScreens(val routes: String) {
    object ProfileScreen : DealSpyScreens("profile_screen")
    object SplashScreen: DealSpyScreens("splash_screen")
}