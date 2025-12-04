package com.example.dealspy.view.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import com.example.dealspy.data.model.Product
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
    val saveForLaterState by searchViewModel.saveForLaterState.collectAsState()
    val addToWatchlistState by watchListViewModel.addToWatchlistState.collectAsState()

    val popularCategories = listOf(
        SearchCategory("T-Shirts", "https://image.hm.com/assets/hm/ec/8f/ec8f4c42235bc4d6dece8e0c82da5aa9800e8e36.jpg?imwidth=1260"),
        SearchCategory("Lipsticks", "https://images-static.nykaa.com/media/catalog/product/7/b/7b8686c8904245710958_2.jpg?tr=w-344,h-344,cm-pad_resize"),
        SearchCategory("Hair Care", "https://cdn.tirabeauty.com/v2/billowing-snowflake-434234/tira-p/wrkr/products/pictures/item/free/resize-w:1080/1163626/caN5Xm1ey_-1163626_1.jpg"),
        SearchCategory("Backpacks", "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/25885877663942eba56602bea225b65e_9366/adidas_Classic_Yay_Sport_Graphic_Backpack_Beige_JX9075_01_00_standard.jpg"),
        SearchCategory("Shoes", "https://dawntown.co.in/cdn/shop/files/off-white-x-air-jordan-1-retro-high-og-chicago-745075.jpg?v=1749480653&width=1062"),
        SearchCategory("iPhone", "https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/iphone-17-pro-finish-select-202509-6-9inch-cosmicorange?wid=1200&hei=700&fmt=webp")
    )

    // Handle save for later state
    LaunchedEffect(saveForLaterState) {
        when (saveForLaterState) {
            is UiState.Success -> {
                Toast.makeText(context, "Saved for later!", Toast.LENGTH_SHORT).show()
                searchViewModel.resetSaveForLaterState()
            }
            is UiState.Error -> {
                Toast.makeText(context, "Error: ${(saveForLaterState as UiState.Error).message}", Toast.LENGTH_SHORT).show()
                searchViewModel.resetSaveForLaterState()
            }
            is UiState.NoInternet -> {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                searchViewModel.resetSaveForLaterState()
            }
            else -> {}
        }
    }

    // Handle add to watchlist state
    LaunchedEffect(addToWatchlistState) {
        when (addToWatchlistState) {
            is UiState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Added to watchlist!",
                        duration = SnackbarDuration.Short
                    )
                }
                watchListViewModel.resetAddState()
            }
            is UiState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to add to watchlist",
                        duration = SnackbarDuration.Long
                    )
                }
                watchListViewModel.resetAddState()
            }
            else -> {}
        }
    }

    LaunchedEffect(searchListState) {
        if (searchListState is UiState.Error) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = (searchListState as UiState.Error).message ?: "Search failed",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Search", navController = navController)
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                bottomMenu = BottomNavOptions.bottomNavOptions
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Search TextField - SEARCH ONLY ON ICON CLICK
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search for products...", style = MaterialTheme.typography.bodyMedium) },
                trailingIcon = {
                    Row {
                        if (query.isNotBlank()) {
                            IconButton(onClick = {
                                query = ""
                            }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        }
                        IconButton(onClick = {
                            if (query.isNotBlank()) {
                                searchViewModel.searchProductList(query)  // ✅ API call ONLY here
                                keyboardController?.hide()
                            }
                        }) {
                            Icon(Icons.Default.Search, "Search")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                // ✅ REMOVED onSearch - no auto search on keyboard
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (query.isNotBlank()) {
                            searchViewModel.searchProductList(query)
                            keyboardController?.hide()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            when {
                query.isBlank() -> {
                    // Show popular categories when no query
                    PopularCategorySection(
                        categories = popularCategories,
                        onCategoryClick = { category ->
                            query = category  // ✅ Fixed: use category.name
                            searchViewModel.searchProductList(category)
                        }
                    )
                }

                searchListState is UiState.Loading -> {
                    // ✅ SHIMMER during loading
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Searching for \"$query\"...",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyColumn {
                            items(count = 6) {
                                ShimmerSearchResultCard(
                                    isLoading = true,
                                    contentAfterLoading = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                searchListState is UiState.Success -> {
                    val products = (searchListState as UiState.Success<List<Product>>).data
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (products.isEmpty()) {
                                "No results found for \"$query\""
                            } else {
                                "Showing ${products.size} results for \"$query\""
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )

                        if (products.isNotEmpty()) {
                            LazyColumn {
                                items(products, key = { it.id ?: it.name ?: it.hashCode().toString() }) { product ->
                                    ShimmerSearchResultCard(
                                        isLoading = false,  // ✅ Show real content
                                        contentAfterLoading = {
                                            SearchResultCard(
                                                product = product,
                                                isSaved = false,
                                                onToggleSave = { productToSave ->
                                                    searchViewModel.toggleSaveForLater(productToSave)
                                                },
                                                onAddToWatch = { productToWatch ->
                                                    watchListViewModel.addToWatchlist(productToWatch)
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
                }

                else -> {
                    // Initial state or error - show minimal shimmer
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Enter search query above",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }
            }
        }
    }
}
