package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.data.model.UiProduct
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.utils.ProductCard
import com.example.dealspy.vm.WatchListViewModel

@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.watchListState.collectAsState()


    Scaffold(
        topBar = {
            AppTopBar(
                title = "Watchlist",
                navController = navController,
            )
        }
    ) { innerPadding ->

        UiStateHandler(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onRetry = { viewModel.loadWatchlist() },
            onSuccess = { products ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(products) { uiProduct ->
                        ProductCard(uiProduct)
                    }
                }
            }
        )
    }
}



@Composable
fun WatchlistScreenPreviewable(products: List<UiProduct>) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Watchlist",
                navController = navController,
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = innerPadding
        ) {
            items(products) { uiProduct ->
                ProductCard(uiProduct)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun WatchlistPreview() {
    val products = listOf(
        UiProduct(
            product = Product(
                name = "Slim Fit Trousers",
                platformName = "Myntra",
                priceRaw = "₹1,259",
                lastKnownPrice = 2990,
                deepLink = "https://myntra.com/product1",
                imageURL = "https://images.unsplash.com/photo-1602810318120-e9b7b0d93d3e?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Allen Solly",
            timeLeftMillis = 2 * 60 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Regular Fit Shirt",
                platformName = "Ajio",
                priceRaw = "₹1,599",
                lastKnownPrice = 3290,
                deepLink = "https://ajio.com/product2",
                imageURL = "https://images.unsplash.com/photo-1584467735871-b93a28c61b43?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Louis Philippe",
            timeLeftMillis = 5 * 60 * 60 * 1000 + 45 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Casual Sneakers",
                platformName = "Flipkart",
                priceRaw = "₹2,499",
                lastKnownPrice = 4990,
                deepLink = "https://flipkart.com/product3",
                imageURL = "https://images.unsplash.com/photo-1561808844-08c53b6bdc02?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "PUMA",
            timeLeftMillis = 3 * 60 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Denim Jacket",
                platformName = "Amazon",
                priceRaw = "₹2,899",
                lastKnownPrice = 5990,
                deepLink = "https://amazon.in/product4",
                imageURL = "https://images.unsplash.com/photo-1552374196-c4e7ffc6e126?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Levi's",
            timeLeftMillis = 45 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Floral Print Dress",
                platformName = "Zara",
                priceRaw = "₹1,799",
                lastKnownPrice = 3490,
                deepLink = "https://zara.com/product5",
                imageURL = "https://images.unsplash.com/photo-1520975661595-6453be3f7070?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "H&M",
            timeLeftMillis = 90 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Leather Handbag",
                platformName = "Nykaa Fashion",
                priceRaw = "₹9,999",
                lastKnownPrice = 15990,
                deepLink = "https://nykaafashion.com/product6",
                imageURL = "https://images.unsplash.com/photo-1571689936005-bac784f3979d?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Michael Kors",
            timeLeftMillis = 4 * 60 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Smart Watch Series 8",
                platformName = "Croma",
                priceRaw = "₹29,999",
                lastKnownPrice = 38999,
                deepLink = "https://croma.com/product7",
                imageURL = "https://images.unsplash.com/photo-1598970434795-0c54fe7c0642?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Apple",
            timeLeftMillis = 5 * 60 * 1000
        ),
        UiProduct(
            product = Product(
                name = "Men's Hoodie",
                platformName = "H&M",
                priceRaw = "₹1,199",
                lastKnownPrice = 2490,
                deepLink = "https://hm.com/product8",
                imageURL = "https://images.unsplash.com/photo-1600185365483-26d7e9248b2b?auto=format&fit=crop&w=400&q=80"
            ),
            brand = "Nike",
            timeLeftMillis = 60 * 60 * 1000
        )
    )

    WatchlistScreenPreviewable(products = products)
}

