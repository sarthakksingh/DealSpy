package com.example.dealspy.view.screens

//TODO: Add add to remove watchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.navigation.DealSpyScreens
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


    val isApiLoading = state is UiState.Loading
    var isPulling by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<UiProduct?>(null) }


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
                indicator = {
                    if (!isApiLoading) {
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
                }
            ) {
                UiStateHandler(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { viewModel.loadWatchlist() },
                    onIdle = {

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
                                ProductCard(
                                    uiProduct = uiProduct,
                                    onCardClick = { productName ->
                                        navController.navigate(
                                            DealSpyScreens.createPriceCompareRoute(
                                                productName
                                            )
                                        )
                                    },
                                    onDelete = { selectedProduct ->

                                        productToDelete = selectedProduct
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                productToDelete = null
            },
            title = {
                Text("Remove from Watchlist")
            },
            text = {
                Text("Are you sure you want to remove '${productToDelete?.product?.name}' from your watchlist?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Actually delete the product
                        productToDelete?.let {
                            viewModel.removeFromWatchlist(it.product.name)
                        }
                        showDeleteDialog = false
                        productToDelete = null
                    }
                ) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        productToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

