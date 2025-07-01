package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dealspy.R
import com.example.dealspy.data.model.Product
import com.example.dealspy.state.UiState
import com.example.dealspy.view.utils.SwipeToDeleteCard
import com.example.dealspy.vm.WatchListViewModel

@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val watchListState by viewModel.watchListState.collectAsState()
    var productList by remember { mutableStateOf(listOf<Product>()) }
    var selectedIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadWatchList()
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddProduct() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        when (watchListState) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    //TODO: ADD LOTTIE ANIMATION
                    CircularProgressIndicator()
                }
            }

            is UiState.Failed -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //TODO: ADD LOTTIE ANIMATION
                }
            }

            is UiState.Success -> {
                val productList = (watchListState as UiState.Success<List<Product>>).data

                LazyColumn(contentPadding = padding) {
                    items(
                        items = productList,
                        key = { it.deepLink }
                    ) { product ->
                        SwipeToDeleteCard(
                            product = product,
                            onClick = { viewModel.onProductClick(product) },
                            onDelete = {
                                viewModel.onDeleteProduct(product)
                            }
                        )
                    }
                }
            }

            else -> {}
        }
    }
}


val dummyProducts = listOf(
    Product(
        name = "iPhone 14",
        platformName = "Amazon",
        priceRaw = "₹71999",
        lastKnownPrice = 75999,
        deepLink = "https://amazon.in/iphone14",
        imageURL = "https://imageurl.com/iphone14.png"
    ),
    Product(
        name = "Nike Air Max",
        platformName = "Flipkart",
        priceRaw = "₹5999",
        lastKnownPrice = 6999,
        deepLink = "https://flipkart.com/nikeair",
        imageURL = "https://imageurl.com/nike.png"
    )
)

// later a function for LottieAnimation
@Composable
fun LottieAnimation() {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.notfounderror))

    if (composition.value != null) {
        LottieAnimation(
            composition = composition.value,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(300.dp)
        )
    } else {
        Text("Animation failed to load", color = Color.Red)
    }
}






