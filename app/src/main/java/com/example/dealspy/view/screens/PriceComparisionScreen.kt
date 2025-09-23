package com.example.dealspy.view.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.ui.state.UiStateHandler
import com.example.dealspy.view.navigation.BottomNavBar
import com.example.dealspy.view.navigation.BottomNavOptions
import com.example.dealspy.view.utils.BestDealCard
import com.example.dealspy.view.utils.RemainingProductCard
import com.example.dealspy.view.utils.ShimmerDealCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.SearchViewModel


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCompareScreen(
    productName: String,
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.priceCompareList.collectAsState()

    LaunchedEffect(productName) {
        viewModel.priceCompare(productName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Price Compare",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Price Compare",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                bottomMenu = BottomNavOptions.bottomNavOptions
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        UiStateHandler(
            state = state,
            onRetry = { viewModel.priceCompare(productName) },
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            onSuccess =
         { products ->
            val sorted = products.sortedBy { it.price }
            val topProduct = sorted.firstOrNull()
            val remainingProducts = sorted.drop(1)

            if (topProduct == null) {
                Text(
                    text = "No Products Found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                return@UiStateHandler
            }

            Column(modifier = Modifier.fillMaxSize()) {
                ShimmerDealCard(isLoading = false, contentAfterLoading = {
                    BestDealCard(topProduct)
                })

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "More price options available:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow {
                    items(remainingProducts) { product ->
                        ShimmerSearchResultCard(
                            isLoading = false,
                            contentAfterLoading = {
                                RemainingProductCard(product)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
        )
    }
}
