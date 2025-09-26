package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.ui.theme.ThemeSelection
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.ProductCard
import com.example.dealspy.vm.WatchListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.watchListState.collectAsState()
    val addState by viewModel.addToWatchlistState.collectAsState()
    val removeState by viewModel.removeFromWatchlistState.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()

    // 🔹 TRACK REFRESH STATE
    val isApiLoading = state is UiState.Loading
    var isPulling by remember { mutableStateOf(false) }

    // 🔹 DETERMINE REFRESH STATE
    val isRefreshing = isApiLoading && isPulling

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadWatchlist()
    }

    LaunchedEffect(addState, removeState) {
        if (addState is UiState.Success || removeState is UiState.Success) {
            viewModel.resetAddState()
            viewModel.resetRemoveState()
        }
    }

    DealSpyTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppTopBar(
                    title = "Watchlist",
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    bottomMenu = BottomNavOptions.bottomNavOptions
                )
            }
        ) { innerPadding ->

            // 🔹 CUSTOM PULL TO REFRESH WITH CENTERED INDICATOR
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isPulling = true
                    viewModel.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                // 🔹 CENTERED PULL-TO-REFRESH INDICATOR
                indicator = {
                    if (!isApiLoading) {
                        // 🔹 CENTER THE PULL-TO-REFRESH INDICATOR
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            PullToRefreshDefaults.Indicator(
                                state = pullToRefreshState,
                                isRefreshing = isRefreshing,
                                modifier = Modifier
                            )
                        }
                    }
                    // 🔹 HIDE INDICATOR WHEN API IS LOADING (SHOW CENTERED ONE INSTEAD)
                }
            ) {
                UiStateHandler(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { viewModel.loadWatchlist() },
                    onIdle = {
                        // 🔹 SHOW CENTERED LOADING WHEN REFRESHING OR INITIAL LOAD
                        if (isApiLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = if (isPulling) "Refreshing..." else "Loading watchlist...",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    },
                    onSuccess = { products ->
                        // 🔹 RESET PULLING STATE WHEN SUCCESS
                        LaunchedEffect(Unit) {
                            isPulling = false
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(products) { uiProduct ->
                                ProductCard(uiProduct)
                            }
                        }
                    }
                )
            }
        }
    }
}

// Your existing preview code remains the same...
