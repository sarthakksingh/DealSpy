// Create: view/screens/SaveForLaterScreen.kt
package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.utils.ProductCard
import com.example.dealspy.vm.SaveForLaterViewModel

@Composable
fun SaveForLaterScreen(
    viewModel: SaveForLaterViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.saveForLaterState.collectAsState()

    // 🔹 SWIPE REFRESH STATE
    val isRefreshing = state is UiState.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    // 🔹 CALL API EVERY TIME SCREEN OPENS
    LaunchedEffect(Unit) {
        viewModel.loadSaveForLater()
    }

    DealSpyTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppTopBar(
                    title = "Save For Later",
                    navController = navController,
                )
            }
        ) { innerPadding ->

            // 🔹 SWIPE REFRESH
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                UiStateHandler(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { viewModel.loadSaveForLater() },
                    onIdle = {
                        if (!isRefreshing) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    },
                    onSuccess = { products ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(products) { uiProduct ->
                                // 🔹 USE YOUR PRODUCT CARD
                                ProductCard(uiProduct = uiProduct)
                            }
                        }
                    }
                )
            }
        }
    }
}
