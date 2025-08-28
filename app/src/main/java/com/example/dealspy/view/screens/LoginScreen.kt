package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.view.components.SignInButton
import com.example.dealspy.vm.LoginViewModel

//add horizontal pager

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()

    when (loginState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
            //TODO: Place a custom progress bar
        }

        is UiState.Success -> {
            // Once login is successful, trigger the success callback
            LaunchedEffect(Unit) {
                onLoginSuccess()
            }
        }

        is UiState.Failed -> {
            val message = (loginState as UiState.Failed).message
            // Replace this with your error UI
            Text(text = "Login failed: $message")
        }
        else -> {}
    }

    // The SignIn Button stays available regardless of state
    SignInButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        loginViewModel = loginViewModel
    )
}
