package com.example.dealspy.view.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.view.components.SignInButton
import com.example.dealspy.vm.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.collectAsState()
    var navigated by remember { mutableStateOf(false) }

    // Notification permission request
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (!isGranted) {
                    Toast.makeText(
                        context,
                        "Please enable notification permissions to receive price alerts.",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
            }
        )

        LaunchedEffect(Unit) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 🔹 USE UiStateHandler - Much cleaner!
    UiStateHandler(
        state = loginState,
        onIdle = {
            // 🔹 IDLE STATE - Show welcome and sign-in button
            WelcomeContent(loginViewModel = loginViewModel)
        },
        onSuccess = { _ ->
            // 🔹 SUCCESS STATE - Navigate to main screen
            LaunchedEffect(loginState) {
                if (!navigated) {
                    navigated = true
                    Toast.makeText(context, "Login successful! Ready for price alerts.", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                    loginViewModel.resetStates()
                }
            }
        },
        onRetry = {
            // 🔹 RETRY - Reset to idle state
            loginViewModel.resetStates()
        }
    )
}

// 🔹 SEPARATE COMPOSABLE for welcome content
@Composable
private fun WelcomeContent(loginViewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Dealspy",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Text(
            text = "Get notified when prices drop on your favorite products",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        SignInButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            loginViewModel = loginViewModel
        )
    }
}
