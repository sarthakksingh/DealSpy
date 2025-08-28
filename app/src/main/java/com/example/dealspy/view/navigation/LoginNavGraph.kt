package com.example.dealspy.view.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.dealspy.view.screens.LoginScreen
import com.example.dealspy.vm.LoginViewModel

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = DealSpyScreens.LoginScreen.routes,
        route = "login_graph"
    ) {
        composable(DealSpyScreens.LoginScreen.routes) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login_graph") { inclusive = true }
                    }
                }
            )
        }
    }
}
