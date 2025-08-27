package com.example.dealspy.view.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.vm.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val isUserLoggedIn = Firebase.auth.currentUser != null
    val startDestination = if (isUserLoggedIn) "main" else "login_graph"
    val loginViewModel: LoginViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = startDestination) {
        loginNavGraph(navController, loginViewModel = loginViewModel)
        mainNavGraph(navController = navController)
    }
}