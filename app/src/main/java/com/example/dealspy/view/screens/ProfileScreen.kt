package com.example.dealspy.view.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.PurchaseHistoryCard
import com.example.dealspy.view.utils.WishlistCard
import com.example.dealspy.vm.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val wishlistState by viewModel.wishlistState.collectAsState()
    val historyState by viewModel.purchaseHistoryState.collectAsState()
    //val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }

    DealSpyTheme {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Profile",
                    navController = navController,
                    onMoreClick = {

                    }
                )
            },
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    bottomMenu = BottomNavOptions.bottomNavOptions
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Use scaffold's inner padding
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "John Doe",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(54.dp))


                Text("Saved for Later", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                UiStateHandler(
                    state = wishlistState,
                    modifier = Modifier.fillMaxWidth(),
                    onRetry = { viewModel.loadWishlist() },
                    onSuccess = { wishlist ->
                        LazyRow {
                            items(wishlist, key = { it.name }) { product ->
                                WishlistCard(product = product, onDelete = {
                                    viewModel.onDeleteFromWishlist(product)
                                })
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(54.dp))


                Text("Purchase History", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                UiStateHandler(
                    state = historyState,
                    modifier = Modifier.fillMaxWidth(),
                    onRetry = { viewModel.loadPurchaseHistory() },
                    onSuccess = { history ->
                        history.forEach { (product, date) ->
                            PurchaseHistoryCard(product = product, date = date)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(54.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Notifications", modifier = Modifier.weight(1f))
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = { viewModel.onClearWatchlist() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface,

                        )
                    ) {
                        Text(
                            text = "Clear History",
                            style = MaterialTheme.typography.bodyMedium,

                        )
                    }


                    Button(
                        onClick = { onLogout() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = "Log Out",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    DealSpyTheme {
        ProfileScreen(
            navController = rememberNavController(),
            onLogout = { /* Mock logout action */ }
        )
    }
}



































////TODO: Have to implement shimmering effect
//@Composable
//fun ProfileScreen(
//    navController: NavController,
//    onLogout: () -> Unit,
//    viewModel: ProfileViewModel = hiltViewModel()
//) {
//    val wishlistState by viewModel.wishlistState.collectAsState()
//    val historyState by viewModel.purchaseHistoryState.collectAsState()
//    val context = LocalContext.current
//    var notificationsEnabled by remember { mutableStateOf(true) }
//
//    DealSpyTheme {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            // 🔙 Back Navigation Button
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back",
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // 👤 Profile Info
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(60.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.primaryContainer),
//                    tint = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Text(
//                    text = "John Doe",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // ❤️ Saved for Later
//            Text("Saved for Later", style = MaterialTheme.typography.titleSmall)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            UiStateHandler(
//                state = wishlistState,
//                modifier = Modifier.fillMaxWidth(),
//                onRetry = { viewModel.loadWishlist() },
//                onSuccess = { wishlist ->
//                    LazyRow {
//                        items(wishlist, key = { it.name }) { product ->
//                            WishlistCard(product = product, onDelete = {
//                                viewModel.onDeleteFromWishlist(product)
//                            })
//                            Spacer(modifier = Modifier.width(12.dp))
//                        }
//                    }
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // 🛍 Purchase History
//            Text("Purchase History", style = MaterialTheme.typography.titleSmall)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            UiStateHandler(
//                state = historyState,
//                modifier = Modifier.fillMaxWidth(),
//                onRetry = { viewModel.loadPurchaseHistory() },
//                onSuccess = { history ->
//                    history.forEach { (product, date) ->
//                        PurchaseHistoryCard(product = product, date = date)
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // ⚙️ App Settings
//            Text("App Settings", style = MaterialTheme.typography.titleSmall)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Enable Notifications", modifier = Modifier.weight(1f))
//                Switch(
//                    checked = notificationsEnabled,
//                    onCheckedChange = { notificationsEnabled = it }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Button(
//                onClick = { viewModel.onClearWatchlist() },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//            ) {
//                Text("Clear Watchlist / History")
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = { onLogout() },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
//            ) {
//                Text("Log out")
//            }
//        }
//    }
//}
//
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ProfileScreenPreview() {
//    // Create mock data for preview
//    val mockWishlist = listOf(
//        Product(
//            name = "Samsung Galaxy Watch 5",
//            platformName = "Amazon",
//            priceRaw = "₹15,999",
//            lastKnownPrice = 18999,
//            deepLink = "https://amazon.in/samsung-watch",
//            imageURL = "https://images.unsplash.com/photo-1523275335684-37898b6baf30"
//        ),
//        Product(
//            name = "Apple AirPods Pro",
//            platformName = "Flipkart",
//            priceRaw = "₹19,900",
//            lastKnownPrice = 24900,
//            deepLink = "https://flipkart.com/airpods-pro",
//            imageURL = "https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb"
//        ),
//        Product(
//            name = "Sony WH-1000XM4",
//            platformName = "Croma",
//            priceRaw = "₹22,990",
//            lastKnownPrice = 29990,
//            deepLink = "https://croma.com/sony-headphones",
//            imageURL = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e"
//        )
//    )
//
//    val mockPurchaseHistory = listOf(
//        Product(
//            name = "iPhone 15 Pro",
//            platformName = "Amazon",
//            priceRaw = "₹134,900",
//            lastKnownPrice = 134900,
//            deepLink = "https://amazon.in/iphone-15-pro",
//            imageURL = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab"
//        ) to "2025-08-15",
//        Product(
//            name = "MacBook Air M2",
//            platformName = "Flipkart",
//            priceRaw = "₹114,900",
//            lastKnownPrice = 119900,
//            deepLink = "https://flipkart.com/macbook-air-m2",
//            imageURL = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef"
//        ) to "2025-07-22"
//    )
//
//    // Mock ViewModel that provides preview data
//    class PreviewProfileViewModel : ProfileViewModel() {
//        init {
//            // Override the StateFlow values with mock data for preview
//            _wishlistState.value = UiState.Success(mockWishlist)
//            _purchaseHistoryState.value = UiState.Success(mockPurchaseHistory)
//        }
//    }
//
//    // Preview with mock NavController and ViewModel
//    DealSpyTheme {
//        ProfileScreen(
//            navController = rememberNavController(),
//            onLogout = { /* Mock logout action */ },
//            viewModel = PreviewProfileViewModel()
//        )
//    }
//}
//
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, name = "Loading State")
//@Composable
//fun ProfileScreenLoadingPreview() {
//    class LoadingProfileViewModel : ProfileViewModel() {
//        init {
//            _wishlistState.value = UiState.Loading
//            _purchaseHistoryState.value = UiState.Loading
//        }
//    }
//
//    DealSpyTheme {
//        ProfileScreen(
//            navController = rememberNavController(),
//            onLogout = { /* Mock logout action */ },
//            viewModel = LoadingProfileViewModel()
//        )
//    }
//}
//
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, name = "Empty State")
//@Composable
//fun ProfileScreenEmptyPreview() {
//    class EmptyProfileViewModel : ProfileViewModel() {
//        init {
//            _wishlistState.value = UiState.Success(emptyList())
//            _purchaseHistoryState.value = UiState.Success(emptyList())
//        }
//    }
//
//    DealSpyTheme {
//        ProfileScreen(
//            navController = rememberNavController(),
//            onLogout = { /* Mock logout action */ },
//            viewModel = EmptyProfileViewModel()
//        )
//    }
//}
//




