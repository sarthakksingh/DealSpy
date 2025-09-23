// Update your UiStateHandler
package com.example.dealspy.ui.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> UiStateHandler(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit,
    onIdle: @Composable () -> Unit = {}, // 🔹 ADD IDLE HANDLER
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    when (state) {
        is UiState.Idle -> {
            // 🔹 HANDLE IDLE STATE
            showDialog = false
            onIdle()
        }

        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is UiState.Success -> {
            showDialog = false
            onSuccess(state.data)
        }

        is UiState.NoData -> {
            CenterMessage("No results found.", modifier)
        }

        is UiState.NoInternet -> {
            errorMessage = "No internet connection."
            showDialog = true
        }

        is UiState.ServerError -> {
            errorMessage = "Internal server error."
            showDialog = true
        }

        is UiState.Error -> {
            errorMessage = state.message
            showDialog = true
        }
    }

    if (showDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showDialog = false },
            onRetry = {
                showDialog = false
                onRetry?.invoke()
            }
        )
    }
}

@Composable
fun CenterMessage(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.9f),
        exit = fadeOut() + scaleOut(targetScale = 0.9f)
    ) {
        AlertDialog(
            onDismissRequest = {
                visible = false
                onDismiss()
            },
            confirmButton = {
                TextButton(onClick = {
                    visible = false
                    onRetry()
                }) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    visible = false
                    onDismiss()
                }) {
                    Text("Dismiss")
                }
            },
            title = { Text("Something went wrong") },
            text = { Text(message) }
        )
    }
}
