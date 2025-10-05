package com.example.dealspy.view.screens

import android.os.Build
import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.SearchCategory
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.components.WatchTimeDialog
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.PopularCategorySection
import com.example.dealspy.view.utils.SearchResultCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.SaveForLaterViewModel
import com.example.dealspy.vm.SearchViewModel
import com.example.dealspy.vm.WatchListViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    saveForLaterViewModel: SaveForLaterViewModel=hiltViewModel(),
    navController: NavController
) {
    DealSpyTheme {
        var query by remember { mutableStateOf("") }
        //var dialogProduct by remember { mutableStateOf<Product?>(null) }

        val keyboardController = LocalSoftwareKeyboardController.current

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val searchListState by searchViewModel.searchList.collectAsState()
        val savedItems by searchViewModel.savedItems.collectAsState()

        val addToWatchlistState by watchListViewModel.addToWatchlistState.collectAsState()
        val removeFromWatchlistState by watchListViewModel.removeFromWatchlistState.collectAsState()

        val popularCategories = listOf(
            SearchCategory("Trendy T-Shirts", "https://images.unsplash.com/photo-1600185364241-d1641d75d5b1"),
            SearchCategory("Bold Lipsticks", "https://images.unsplash.com/photo-1598214886801-617ec3b86a9d"),
            SearchCategory("Hair Care", "https://images.unsplash.com/photo-1615461066841-1bb63b0cf72e"),
            SearchCategory("Backpacks", "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f"),
            SearchCategory("Tshirts", "https://images.unsplash.com/photo-1584467735871-594d63f4f01b"),
            SearchCategory("Sweatshirts", "https://images.unsplash.com/photo-1618354691327-3c0c4b773482"),
            SearchCategory("Moisturisers", "https://images.unsplash.com/photo-1616745307545-b90f46bcb8b3"),
            SearchCategory("Cups & Mugs", "https://images.unsplash.com/photo-1507914372432-45d43f3b1590")
        )

        LaunchedEffect(addToWatchlistState) {
            when (addToWatchlistState) {
                is UiState.Success -> {
                    Log.d("SearchScreen", "Product added to watchlist successfully")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Product added to watchlist!",
                            duration = SnackbarDuration.Short
                        )
                    }
                    watchListViewModel.resetAddState()
                }
                is UiState.Error -> {
                    Log.e("SearchScreen", "Failed to add product to watchlist: ${(addToWatchlistState as UiState.Error).message}")
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Failed to add to watchlist: ${(addToWatchlistState as UiState.Error).message}",
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
            when (removeFromWatchlistState) {
                is UiState.Success -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Product removed from watchlist!",
                            duration = SnackbarDuration.Short
                        )
                    }
                    watchListViewModel.resetRemoveState()
                }
                is UiState.Error -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Failed to remove from watchlist: ${(removeFromWatchlistState as UiState.Error).message}",
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
                            Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
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
                                                    isSaved = savedItems.contains(product.deepLink), // FIX: Use actual saved state
                                                    onToggleSave = { productToSave ->
                                                        searchViewModel.toggleSaveForLater(productToSave) // FIX: Use SearchViewModel method
                                                    },
                                                    onAddToWatch = { productToWatch ->
                                                        //dialogProduct = productToWatch
                                                        watchListViewModel.addToWatchlist(product)
                                                        Log.d("SearchScreen", "Opening watch dialog for: ${productToWatch.name}")
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

//        dialogProduct?.let { product ->
//            WatchTimeDialog(
//                product = product,
//                onDismiss = {
//                    Log.d("SearchScreen", "Watch dialog dismissed")
//                    dialogProduct = null
//                },
//                onConfirm = { days ->
//                    Log.d("SearchScreen", "Adding ${product.name} to watchlist for $days days")
//
//                    val watchEndDate = LocalDate.now().plusDays(days.toLong())
//
//                    watchListViewModel.addToWatchlist(product, watchEndDate)
//
//                    dialogProduct = null
//                }
//            )
//        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    DealSpyTheme(theme = com.example.dealspy.ui.theme.ThemeSelection.Option2) {
        SearchScreen(navController = rememberNavController())
    }
}