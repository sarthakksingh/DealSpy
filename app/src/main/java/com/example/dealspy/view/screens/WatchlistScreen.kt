package com.example.dealspy.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.ShimmerWatchCard
import com.example.dealspy.view.utils.WatchCard
import com.example.dealspy.vm.WatchListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val watchListState by viewModel.watchlist.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current
    val isApiLoading = watchListState is UiState.Loading
    var isPulling by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    val isRefreshing = isApiLoading && isPulling

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.getWatchlistProducts()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = "Watchlist", navController = navController)
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
                viewModel.getWatchlistProducts()
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
                            isRefreshing = false,
                            modifier = Modifier
                        )
                    }
                }
            }
        ) {
            when {
                watchListState is UiState.Loading -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = 6) {
                            ShimmerWatchCard(
                                isLoading = true,
                                contentAfterLoading = {}
                            )
                        }
                    }
                }

                else -> {
                    UiStateHandler(
                        state = watchListState,
                        modifier = Modifier.fillMaxSize(),
                        onRetry = { viewModel.getWatchlistProducts() },
                        onIdle = {
                            if (isApiLoading) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    repeat(4) {
                                        ShimmerWatchCard(
                                            isLoading = true,
                                            contentAfterLoading = {},
                                            modifier = Modifier
                                                .padding(8.dp)
                                        )
                                    }
                                }
                            }
                        },
                        onSuccess = { products ->
                            LaunchedEffect(Unit) {
                                isPulling = false
                            }

                            if (products.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No items in watchlist",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = products,
                                        key = { it.name ?: "Null" }
                                    ) { product ->
                                        WatchCard(
                                            context = context,
                                            product = product,
                                            onDelete = {
                                                Log.d(
                                                    "WatchlistScreen",
                                                    "✅ onDelete CLICKED for ${product.name}"
                                                )
                                                productToDelete = product
                                                showDeleteDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
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
                        Text(
                            "Are you sure you want to remove '${productToDelete?.name}' from your watchlist?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                Log.d(
                                    "WatchlistScreen",
                                    "✅ REMOVE BUTTON CLICKED for ${productToDelete?.name}"
                                )
                                productToDelete?.name?.let {
                                    Log.d(
                                        "WatchlistScreen",
                                        "✅ CALLING ViewModel.removeFromWatchlist($it)"
                                    )
                                    viewModel.removeFromWatchlist(it)
                                }
                                showDeleteDialog = false
                                productToDelete = null
                            },
                            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Remove")
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
    }
}
