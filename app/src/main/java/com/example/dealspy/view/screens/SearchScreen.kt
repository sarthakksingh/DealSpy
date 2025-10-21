package com.example.dealspy.view.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.data.model.SearchCategory
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.PopularCategorySection
import com.example.dealspy.view.utils.SearchResultCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.SearchViewModel
import com.example.dealspy.vm.WatchListViewModel
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

        var query by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val searchListState by searchViewModel.searchList.collectAsState()
        val savedItems by searchViewModel.savedItems.collectAsState()
        val saveForLaterState by searchViewModel.saveForLaterState.collectAsState()
        val addToWatchlistState by watchListViewModel.addToWatchlistState.collectAsState()
        val removeFromWatchlistState by watchListViewModel.removeFromWatchlistState.collectAsState()


        val popularCategories = listOf(
            SearchCategory("T-Shirts", "https://image.hm.com/assets/hm/ec/8f/ec8f4c42235bc4d6dece8e0c82da5aa9800e8e36.jpg?imwidth=1260"),
            SearchCategory("Lipsticks", "https://www.chanel.com/images//t_one//w_0.38,h_0.38,c_crop/q_auto:good,f_autoplus,fl_lossy,dpr_1.1/w_1020/rouge-allure-laque-ultrawear-shine-liquid-lip-colour-87-rouge-irregulier-0-18fl-oz--packshot-default-165087-8840425406494.jpg"),
            SearchCategory("Hair Care", "https://cdn.tirabeauty.com/v2/billowing-snowflake-434234/tira-p/wrkr/products/pictures/item/free/resize-w:1080/1163626/caN5Xm1ey_-1163626_1.jpg"),
            SearchCategory("Backpacks", "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/25885877663942eba56602bea225b65e_9366/adidas_Classic_Yay_Sport_Graphic_Backpack_Beige_JX9075_01_00_standard.jpg"),
            SearchCategory("Shoes", "https://dawntown.co.in/cdn/shop/files/off-white-x-air-jordan-1-retro-high-og-chicago-745075.jpg?v=1749480653&width=1062"),
            SearchCategory("iphone", "https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-17-pro-finish-select-202509-6-9inch-cosmicorange?wid=5120&hei=2880&fmt=webp&qlt=90&.v=NUNzdzNKR0FJbmhKWm5YamRHb05tUzkyK3hWak1ybHhtWDkwUXVINFc0RnVrUzFnTVVSUnNLVnZUWUMxNTBGaGhsQTdPYWVGbmdIenAvNE9qYmZVYVFDb1F2RTNvUEVHRkpGaGtOSVFHak5NTEhXRE11VU1QNVo2eDJsWlpuWHQyaWthYXpzcEpXMExJLy9GTE9wWkNn&traceId=1"),
            SearchCategory("Beauty Products", "https://m.media-amazon.com/images/I/51veXCSCAJL._SX679_.jpg"),
            SearchCategory("Cups & Mugs", "https://nestasia.in/cdn/shop/products/DIN03-VERAPINK-BN820CN_4.jpg?v=1657889646&width=600")
        )


        LaunchedEffect(saveForLaterState) {
            val currentState = saveForLaterState
            when (currentState) {
                is UiState.Success -> {
                    Toast.makeText(context, currentState.data, Toast.LENGTH_SHORT).show()
                    searchViewModel.resetSaveForLaterState()
                }
                is UiState.Error -> {
                    Toast.makeText(context, "Error: ${currentState.message}", Toast.LENGTH_SHORT).show()
                    searchViewModel.resetSaveForLaterState()
                }
                is UiState.NoInternet -> {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                    searchViewModel.resetSaveForLaterState()
                }
                else -> { /* Do nothing for Loading and Idle states */ }
            }
        }


        LaunchedEffect(addToWatchlistState) {
            val currentState = addToWatchlistState
            when (currentState) {
                is UiState.Success -> {
                    Log.d("SearchScreen", "Product added to watchlist successfully")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = currentState.data.toString(),
                            duration = SnackbarDuration.Short
                        )
                    }
                    watchListViewModel.resetAddState()
                }
                is UiState.Error -> {
                    Log.e("SearchScreen", "Failed to add product to watchlist: ${currentState.message}")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Failed to add to watchlist: ${currentState.message}",
                            duration = SnackbarDuration.Long
                        )
                    }
                    watchListViewModel.resetAddState()
                }
                is UiState.Loading -> {
                    Log.d("SearchScreen", "Adding product to watchlist...")
                }
                else -> {}
            }
        }


        LaunchedEffect(removeFromWatchlistState) {
            val currentState = removeFromWatchlistState
            when (currentState) {
                is UiState.Success -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = currentState.data.toString(),
                            duration = SnackbarDuration.Short
                        )
                    }
                    watchListViewModel.resetRemoveState()
                }
                is UiState.Error -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Failed to remove from watchlist: ${currentState.message}",
                            duration = SnackbarDuration.Long
                        )
                    }
                    watchListViewModel.resetRemoveState()
                }
                else -> {}
            }
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Search",
                    navController = navController
                )
            },
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    bottomMenu = BottomNavOptions.bottomNavOptions
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            "Search for products...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                if (query.isNotBlank()) {
                                    searchViewModel.searchProductList(query)
                                    keyboardController?.hide()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (query.isNotBlank()) {
                                searchViewModel.searchProductList(query)
                                keyboardController?.hide()
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                if (query.isBlank()) {
                    PopularCategorySection(
                        categories = popularCategories,
                        onCategoryClick = { category ->
                            query = category
                            searchViewModel.searchProductList(category)
                        }
                    )
                } else {
                    UiStateHandler(
                        state = searchListState,
                        modifier = Modifier.fillMaxSize(),
                        onRetry = { searchViewModel.searchProductList(query) },
                        onSuccess = { products ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Showing results for \"$query\"",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyColumn {
                                    items(products) { product ->
                                        ShimmerSearchResultCard(
                                            isLoading = false,
                                            contentAfterLoading = {
                                                SearchResultCard(
                                                    product = product,
                                                    isSaved = savedItems.contains(product.deepLink),
                                                    onToggleSave = { productToSave ->
                                                        searchViewModel.toggleSaveForLater(productToSave)
                                                    },
                                                    onAddToWatch = { productToWatch ->
                                                        watchListViewModel.addToWatchlist(product)
                                                        Log.d("SearchScreen", "Adding to watchlist: ${productToWatch.name}")
                                                    }
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }



