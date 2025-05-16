package com.example.dealspy.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.view.Screens.ProfileScreen
import com.example.dealspy.view.Screens.SplashScreen

@Composable
fun DealSpyNavigation(
    navController: NavHostController = rememberNavController()
) {
    val startDestination = DealSpyScreens.SplashScreen.routes
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(DealSpyScreens.SplashScreen.routes) {
            SplashScreen(navController = navController)
        }
        composable (DealSpyScreens.ProfileScreen.routes){
            ProfileScreen(navController = navController)
        }
    }
}