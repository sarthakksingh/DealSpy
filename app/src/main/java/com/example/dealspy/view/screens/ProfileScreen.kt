package com.example.dealspy.view.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.utils.PurchaseHistoryCard
import com.example.dealspy.view.utils.WishlistCard
import com.example.dealspy.vm.ProfileViewModel

//TODO: Have to implement shimmering effect
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val wishlistState by viewModel.wishlistState.collectAsState()
    val historyState by viewModel.purchaseHistoryState.collectAsState()
    val context = LocalContext.current

    DealSpyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 👤 Profile Info
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
                    text = "John Doe", // You can load from state later
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ❤️ Saved for Later
            Text("Saved for Later", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            UiStateHandler(
                state = wishlistState,
                modifier = Modifier.fillMaxWidth(),
                onRetry = { viewModel.loadWishlist() },
                onSuccess =
             { wishlist ->
                LazyRow {
                    items(wishlist, key = { it.name }) { product ->
                        WishlistCard(product = product, onDelete = {
                            viewModel.onDeleteFromWishlist(product)
                        })
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            })

            Spacer(modifier = Modifier.height(24.dp))

            // 🛍 Purchase History
            Text("Purchase History", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            UiStateHandler(
                state = historyState,
                modifier = Modifier.fillMaxWidth(),
                onRetry = { viewModel.loadPurchaseHistory() },
                onSuccess =
             { history ->
                history.forEach { (product, date) ->
                    PurchaseHistoryCard(product = product, date = date)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            })

            Spacer(modifier = Modifier.height(24.dp))

            // ⚙️ App Settings
            Text("App Settings", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            var notificationsEnabled by remember { mutableStateOf(true) }

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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.onClearWatchlist() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Clear Watchlist / History")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.signOut(context = context, onComplete = {
                        // navController.popBackStack()
                    })
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Log out")
            }
        }
    }
}





