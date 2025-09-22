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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // 🔹 ADD LOADING STATE FOR FIREBASE INITIALIZATION
    var isLoading by remember { mutableStateOf(true) }
    var isUserLoggedIn by remember { mutableStateOf(false) }

    // 🔹 WAIT FOR FIREBASE TO INITIALIZE AND RESTORE AUTH STATE
    LaunchedEffect(Unit) {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            isUserLoggedIn = auth.currentUser != null
            isLoading = false // Firebase has initialized
        }

        Firebase.auth.addAuthStateListener(authStateListener)
    }

    // 🔹 SHOW LOADING WHILE FIREBASE INITIALIZES
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 🔹 NAVIGATION AFTER FIREBASE IS READY
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
