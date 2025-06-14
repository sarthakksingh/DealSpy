package com.example.dealspy.view.screens


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var productList by remember { mutableStateOf(listOf<Product>()) }
    var selectedIndex by remember { mutableStateOf(0) }

    val navItemList = listOf(
        NavItems("watchList", Icons.Default.Home),
        NavItems("Search", Icons.Default.Search),
        NavItems("Profile", Icons.Default.AccountCircle)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navItem.label + "_screen")
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label
                            )
                        },
                        label = {
                            Text(
                                navItem.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
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




