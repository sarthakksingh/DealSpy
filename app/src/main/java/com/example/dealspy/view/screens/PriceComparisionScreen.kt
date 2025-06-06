package com.example.dealspy.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dealspy.data.model.Product
import com.example.dealspy.state.UiState
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.utils.ShimmerDealCard
import com.example.dealspy.view.utils.ShimmerSearchResultCard
import com.example.dealspy.vm.MainViewModel

val bgColor = Color(0xFFFFF3EF)
val highlightColor = Color(0xFFFFC8C8)
val textColor = Color(0xFF222222)
val cardColor = Color(0xFFF9F9F9)
val accentColor = Color(0xFF4CAF50) // Green for price

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceComparePreviewScreen(products: List<Product>, viewModel: MainViewModel) {
    DealSpyTheme {
        val sortedList = products.sortedBy { it.price }
        var isLoading by remember { mutableStateOf(false) }
        val topProduct = sortedList.firstOrNull()
        val remainingProducts = sortedList.drop(1)
        val priceCompareList = viewModel.priceCompareList.collectAsState()

        isLoading = when(priceCompareList.value){
            is UiState.Loading ->{
                true
            }
            else -> {
                false
            }
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
                                imageVector = Icons.Default.ShoppingCart, // or Icons.Default.Compare
                                contentDescription = "Price Compare",
                                tint = textColor
                            )
                            Text(
                                "Price Compare",
                                color = textColor,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    },
                    modifier = Modifier.background(bgColor),
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = bgColor
                    )
                )
            },
            containerColor = bgColor
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
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "More price options available:",
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow {
                        items(remainingProducts) { product ->
                            ShimmerSearchResultCard(isLoading = isLoading, contentAfterLoading = {
                                RemainingProductCard(product)
                            }, modifier = Modifier.fillMaxWidth().padding(16.dp))

                        }
                    }
                } ?: Text("No Products Found", color = textColor)
            }
        }
    }
}

@Composable
fun BestDealCard(product: Product){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = product.imageURL,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(130.dp)
                    .background(highlightColor, shape = RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    "BEST DEAL",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Name: ${product.name}",
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text("Price:", color = textColor, fontWeight = FontWeight.Bold)
                    Text(
                        "₹${product.price}",
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Sold by: ${product.platformName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }
    }
}
@Composable
fun RemainingProductCard(product: Product){
    Card(
        modifier = Modifier
            .width(240.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = product.imageURL,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(70.dp)
                    .background(highlightColor, RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = accentColor
                )
                Text(
                    text = "Sold by: ${product.platformName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }
    }
}