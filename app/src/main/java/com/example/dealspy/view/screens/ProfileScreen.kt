package com.example.dealspy.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dealspy.data.model.Product
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.ui.theme.ThemeSelection
import com.example.dealspy.view.components.AppTopBar
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.WishlistCard
import com.example.dealspy.vm.ProfileViewModel
import com.example.dealspy.vm.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val wishlistState by viewModel.wishlist.collectAsState()
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }

    val themeVm: ThemeViewModel = hiltViewModel()
    val currentTheme by themeVm.theme.collectAsState()
    var showThemeSheet by remember { mutableStateOf(false) }

    val deleteUserState by viewModel.deleteUserState.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deleteUserState) {
        when (deleteUserState) {
            is UiState.Success -> {
                Toast.makeText(
                    context,
                    "Account deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetDeleteState()
                FirebaseAuth.getInstance().signOut()
                onLogout()
            }
            is UiState.Error -> {
                val errorState = deleteUserState as UiState.Error
                Toast.makeText(
                    context,
                    "Failed to delete account: ${errorState.message}",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetDeleteState()
            }
            is UiState.NoInternet -> {
                Toast.makeText(
                    context,
                    "No internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetDeleteState()
            }
            else -> { /* Idle or Loading */ }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Profile",
                navController = navController,
                onMoreClick = {}
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
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val photoUrl = viewModel.getUserPhotoUrl()
                if (photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.getUserDisplayName(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = viewModel.getUserEmail() ?: "user@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Wishlist Section
            Text("Saved Items", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            when (wishlistState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val products = (wishlistState as UiState.Success<List<Product>>).data
                    if (products.isEmpty()) {
                        Text(
                            text = "No saved items yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = products,
                                key = { it.name }
                            ) { product ->
                                WishlistCard(
                                    product = product,
                                    onDelete = { viewModel.removeFromWishlist(product.name)},
                                        onAddToWatchlist = {}

                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = "Error loading saved items",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UiState.NoData -> {
                    Text(
                        text = "No saved items yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notifications
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

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Button
            Button(
                onClick = { showThemeSheet = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Change Theme")
            }

            if (showThemeSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showThemeSheet = false }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Select Theme", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))

                        val options = listOf(
                            ThemeSelection.Theme1 to "Stellar Navy",
                            ThemeSelection.Theme2 to "Urban Ember",
                            ThemeSelection.Theme3 to "Aqua Zenith",
                            ThemeSelection.Theme4 to "Obsidian Whisper",
                            ThemeSelection.Theme14 to "Dusk Platinum",
                            ThemeSelection.Theme15 to "Graphite Noir",
                            ThemeSelection.Theme16 to "Midnight Serene",
                            ThemeSelection.Theme17 to "Rich Umber",
                            ThemeSelection.Theme18 to "Deep Forest Whisper",
                            ThemeSelection.Theme5 to "Veridian Depth",
                            ThemeSelection.Theme6 to "Neon Dusk",
                            ThemeSelection.Theme6 to "Neon Dusk",
                            ThemeSelection.Theme10 to "Midnight Bloom",//logout button
                            ThemeSelection.Theme11 to "Charcoal Horizon",
                            ThemeSelection.Theme12 to "Nightfall Velvet",
                            ThemeSelection.Theme8 to "Lunar Dust"
                        )

                        options.forEach { (value, label) ->
                            val isSelected = currentTheme == value
                            TextButton(
                                onClick = {
                                    themeVm.setTheme(value)
                                    showThemeSheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                color = if (isSelected) Color.White else Color.Transparent,
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = deleteUserState !is UiState.Loading
                ) {
                    if (deleteUserState is UiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Delete Profile",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
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

            if (showDeleteConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmDialog = false },
                    title = { Text("Delete Account?") },
                    text = {
                        Text(
                            "This will permanently delete your account and all associated data. " +
                                    "This action cannot be undone.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteConfirmDialog = false
                                viewModel.deleteUserAccount()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
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
            onLogout = { }
        )
    }
}
