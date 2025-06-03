package com.example.dealspy.view.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.dealspy.data.model.Product
import com.example.dealspy.view.utils.SwipeToDeleteCard


@Composable
fun WatchlistScreen(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onAddProduct: () -> Unit,
    onDeleteProduct: (Product) -> Unit
) {
    var productList by remember { mutableStateOf(products) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(
                items = productList,
                key = { it.deepLink }
            ) { product ->
                SwipeToDeleteCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onDelete = {
                        productList = productList.filterNot { it.deepLink == product.deepLink }
                        onDeleteProduct(it)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WatchlistScreenPreview() {
    val dummyProducts = listOf(
        Product(
            name = "Redmi Note 13 Pro",
            platformName = "Flipkart",
            price = 21999,
            lastKnownPrice = 23999,
            deepLink = "https://www.flipkart.com/redmi-note-13-pro",
            imageURL = "https://rukminim2.flixcart.com/image/416/416/xif0q/mobile/n/l/x/-original-imagx9egb3wqkhxc.jpeg"
        ),
        Product(
            name = "Samsung Galaxy M14",
            platformName = "Amazon",
            price = 10499,
            lastKnownPrice = 11499,
            deepLink = "https://www.amazon.in/samsung-galaxy-m14",
            imageURL = "https://m.media-amazon.com/images/I/81ZSn2rk9WL._SX679_.jpg"
        )
    )

    WatchlistScreen(
        products = dummyProducts,
        onProductClick = {},
        onAddProduct = {},
        onDeleteProduct = {}
    )
}


