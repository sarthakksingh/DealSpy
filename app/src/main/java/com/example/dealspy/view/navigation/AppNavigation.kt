package com.example.dealspy.view.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.view.navigation.loginNavGraph
import com.example.dealspy.view.navigation.mainNavGraph
import com.example.dealspy.view.screens.SplashScreen
import com.example.dealspy.vm.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen { showSplash = false }
        return
    }
    val currentUser = Firebase.auth.currentUser
    Log.d("AppNavigation", "🔥 NAVIGATION CHECK:")
    Log.d("AppNavigation", "Current user: ${currentUser?.email}")
    Log.d("AppNavigation", "Is logged in: ${currentUser != null}")
    val isUserLoggedIn = currentUser != null
    val startDestination = if (isUserLoggedIn) "main" else "login_graph"
    Log.d("AppNavigation", "Start destination: $startDestination")
    val loginViewModel: LoginViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        loginNavGraph(navController, loginViewModel = loginViewModel)
        mainNavGraph(navController = navController)
    }
}
