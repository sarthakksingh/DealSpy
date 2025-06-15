package com.example.dealspy.view.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.data.model.Product
import com.example.dealspy.state.UiState
import com.example.dealspy.view.utils.BestDealCard
import com.example.dealspy.view.utils.RemainingProductCard
import com.example.dealspy.view.utils.ShimmerDealCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.SearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCompareScreen(
    productName: String,
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(productName) {
        viewModel.priceCompare(productName)
    }

    val priceCompareState by viewModel.priceCompareList.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    var sortedList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isFailed by remember { mutableStateOf(false) }

    when (priceCompareState) {
        is UiState.Success -> {
            val data = (priceCompareState as UiState.Success<List<Product>>).data
            sortedList = data.sortedBy { it.price }
            isFailed = false
            isLoading = false
        }
        is UiState.Failed -> {
            sortedList = emptyList()
            isFailed = true
            isLoading = false
        }
        else -> {
            // keep shimmer as is
            isLoading = true
            isFailed = false
        }
    }


    val topProduct = sortedList.firstOrNull()
    val remainingProducts = sortedList.drop(1)

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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            topProduct?.let {
                ShimmerDealCard(isLoading = isLoading, contentAfterLoading = {
                    BestDealCard(it)
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
                            isLoading = isLoading,
                            contentAfterLoading = {
                                RemainingProductCard(product)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            } ?: // TODO: Have to implement lottie animation for no product found
            Text(
                text = "No Products Found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
