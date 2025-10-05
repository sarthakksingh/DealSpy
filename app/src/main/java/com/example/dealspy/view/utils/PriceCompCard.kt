package com.example.dealspy.view.utils

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.dealspy.data.model.Product
import com.example.dealspy.ui.theme.DealSpyTheme

@Composable
fun PriceComparisonCard(
    modifier: Modifier = Modifier,
    product: Product,
    isLowestPrice: Boolean = false,

) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isLowestPrice) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isLowestPrice)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surfaceContainer
        ),
        border = if (isLowestPrice)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = product.imageURL,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))


            Column(
                modifier = Modifier.weight(1f)
            ) {

                if (isLowestPrice) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "BEST PRICE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }


                Text(
                    text = product.platformName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.priceRaw,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isLowestPrice) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )


                    if (product.lastKnownPrice > product.price) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.lastKnownPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }


                if (product.lastKnownPrice > product.price) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        val discountPercent = ((product.lastKnownPrice - product.price).toFloat() / product.lastKnownPrice * 100).toInt()
                        Text(
                            text = "$discountPercent% OFF",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )


                        Text(
                            text = "Save ₹${product.lastKnownPrice - product.price}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, product.deepLink.toUri())
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Handle error - could show a toast
                    }
                },
                modifier = Modifier.width(80.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Visit Store",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Visit",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceComparisonCardPreview() {
    DealSpyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Best price card
            PriceComparisonCard(
                product = Product(
                    name = "iPhone 15 Pro",
                    platformName = "Amazon",
                    priceRaw = "₹134,900",
                    lastKnownPrice = 149900,
                    deepLink = "https://amazon.in/iphone",
                    imageURL = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab"
                ),
                isLowestPrice = true
            )

            // Regular card
            PriceComparisonCard(
                product = Product(
                    name = "iPhone 15 Pro",
                    platformName = "Flipkart",
                    priceRaw = "₹139,999",
                    lastKnownPrice = 149900,
                    deepLink = "https://flipkart.com/iphone",
                    imageURL = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab"
                ),
                isLowestPrice = false
            )

            // Card with no discount
            PriceComparisonCard(
                product = Product(
                    name = "iPhone 15 Pro",
                    platformName = "Croma",
                    priceRaw = "₹149,999",
                    lastKnownPrice = 149999,
                    deepLink = "https://croma.com/iphone",
                    imageURL = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab"
                ),
                isLowestPrice = false
            )
        }
    }
}
