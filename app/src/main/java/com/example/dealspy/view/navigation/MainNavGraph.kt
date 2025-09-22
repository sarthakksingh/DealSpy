package com.example.dealspy.view.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.dealspy.view.screens.PriceCompareScreen
import com.example.dealspy.view.screens.ProfileScreen
import com.example.dealspy.view.screens.SearchScreen
import com.example.dealspy.view.screens.WatchlistScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    // 🔹 CREATE NESTED NAVIGATION GRAPH WITH "main" ROUTE
    navigation(
        startDestination = DealSpyScreens.WatchListScreen.routes, // Default screen after login
        route = "main" // This matches the route you're navigating to!
    ) {
        composable(DealSpyScreens.WatchListScreen.routes) {
            WatchlistScreen(navController = navController)
        }

        composable(DealSpyScreens.SearchScreen.routes) {
            SearchScreen(navController = navController)
        }

        composable(DealSpyScreens.ProfileScreen.routes) {
            ProfileScreen(navController, onLogout = {
                Firebase.auth.signOut()
                navController.navigate("login_graph") {
                    popUpTo("main") { inclusive = true }
                }
            })
        }

        composable(
            route = "${DealSpyScreens.PriceCompareScreen.routes}/{product}",
            arguments = listOf(navArgument("product") { type = NavType.StringType })
        ) { backStackEntry ->
            val product = backStackEntry.arguments?.getString("product")
            product?.let {
                PriceCompareScreen(productName = it, navController = navController)
            }
        }
    }
}
