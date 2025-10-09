package com.example.dealspy.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.example.dealspy.view.utils.PriceComparisonCard
import com.example.dealspy.vm.PriceComparisonViewModel

@Composable
fun PriceCompareScreen(
    productName: String,
    navController: NavController,
    viewModel: PriceComparisonViewModel = hiltViewModel()
) {
    val priceComparisonState by viewModel.priceComparisonState.collectAsState()


    LaunchedEffect(productName) {
        viewModel.loadPriceComparison(productName)
    }


        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Price Comparison",
                    navController = navController,
                    onMoreClick = {
                        // Handle more menu - could add sort options, filters, etc.
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
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                Text(
                    text = productName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )




                UiStateHandler(
                    state = priceComparisonState,
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { viewModel.retryPriceComparison() },
                    onSuccess = { products ->
                        if (products.isEmpty()) {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "No price comparisons found",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Try again or check your internet connection",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(products) { index, product ->
                                    PriceComparisonCard(
                                        product = product,
                                        isLowestPrice = index == 0,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PriceCompareScreenPreview() {
    DealSpyTheme {
        PriceCompareScreen(
            productName = "iPhone 15 Pro",
            navController = rememberNavController()
        )
    }
}
