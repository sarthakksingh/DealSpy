package com.example.dealspy.view.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.view.navigation.NavItems
import com.example.dealspy.view.utils.SwipeToDeleteCard


@Composable
fun WatchlistScreen(
    onProductClick: (Product) -> Unit,
    onAddProduct: () -> Unit,
    onDeleteProduct: (Product) -> Unit,
    navController: NavController
) {
    var products: List<Product>
    var productList by remember { mutableStateOf(products) }
    var selectedIndex by remember { mutableStateOf(0) }
    val navItemList = listOf(
        NavItems("watchList", Icons.Default.Home),
        NavItems("Search", Icons.Default.Search),
        NavItems("Profile", Icons.Default.AccountCircle)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navItem.label+"_screen")
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = "Icon"
                            )
                        },
                        label = {
                            Text(navItem.label)
                        }
                    )
                }
            }
        },
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


/*@Preview(showBackground = true)
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
}*/


