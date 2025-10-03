package com.example.dealspy.ui.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> UiStateHandler(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit,
    onIdle: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    when (state) {
        is UiState.Idle -> {
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No results found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pull down to refresh",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
