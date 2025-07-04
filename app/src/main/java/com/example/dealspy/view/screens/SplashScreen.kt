package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dealspy.view.components.SignInButton

@Composable
fun SplashScreen(navController: NavHostController){
    SignInButton(modifier = Modifier.fillMaxWidth().height(48.dp), navController = navController)
}