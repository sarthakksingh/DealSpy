package com.example.dealspy.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dealspy.view.screens.PriceCompareScreen
import com.example.dealspy.view.screens.ProfileScreen
import com.example.dealspy.view.screens.SearchScreen
import com.example.dealspy.view.screens.WatchlistScreen

@Composable
fun mainNavGraph(navController: NavHostController = rememberNavController()) {
    val startDestination = DealSpyScreens.WatchListScreen.routes
    NavHost(navController, startDestination) {
        composable(DealSpyScreens.WatchListScreen.routes) {
            WatchlistScreen(
                navController = navController,
                onProductClick = TODO(),
                onAddProduct = TODO(),
                onDeleteProduct = TODO()
            )
        }
        composable(DealSpyScreens.ProfileScreen.routes) {
           // ProfileScreen(navController = navController)
        }
        composable(DealSpyScreens.SearchScreen.routes) {
            SearchScreen(navController = navController)
        }
        val detailScreen = DealSpyScreens.PriceCompareScreen.routes
        composable(
            route = "$detailScreen/{productName}", arguments = listOf(
            navArgument("product") {
                type = NavType.StringType
            }
        )) { backStackEntry ->
            backStackEntry.arguments?.getString("productName").let {
                if (it != null)
                    PriceCompareScreen(
                        productName = it,
                        navController = navController
                    )
            }
        }
    }
}