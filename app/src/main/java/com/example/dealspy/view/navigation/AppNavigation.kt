package com.example.dealspy.view.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.vm.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    var isLoading by remember { mutableStateOf(true) }
    var isUserLoggedIn by remember { mutableStateOf(false) }

    // 🔹 WAIT FOR FIREBASE TO PROPERLY RESTORE AUTH STATE
    LaunchedEffect(Unit) {
        // Give Firebase time to restore persisted auth
        delay(2000) // 2 seconds should be enough

        // Check auth state after Firebase has initialized
        isUserLoggedIn = Firebase.auth.currentUser != null
        isLoading = false

        println("🔥 Auth check complete - User logged in: $isUserLoggedIn")
        println("🔥 Current user: ${Firebase.auth.currentUser?.email}")
    }

    // 🔹 SHOW LOADING SCREEN WHILE FIREBASE INITIALIZES
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (isUserLoggedIn) "main" else "login_graph"
    val loginViewModel: LoginViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        loginNavGraph(navController, loginViewModel = loginViewModel)
        mainNavGraph(navController = navController)
    }
}
