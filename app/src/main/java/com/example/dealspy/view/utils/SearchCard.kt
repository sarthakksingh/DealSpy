package com.example.dealspy.view.utils

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dealspy.data.model.Product

@Composable
fun SearchResultCard(
    product: Product,
    isSaved: Boolean,
    onToggleSave: (Product) -> Unit,
    onAddToWatch: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Box(modifier = Modifier.size(90.dp)) {
                AsyncImage(
                    model = product.imageURL,
                    contentDescription = product.name,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(90.dp),
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Save for Later",
                    tint = if (isSaved) Color(0xFFFF4081) else Color.Gray,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clickable { onToggleSave(product) }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(product.platformName, style = MaterialTheme.typography.labelSmall)

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "₹${product.price}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (product.lastKnownPrice > product.price) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "₹${product.lastKnownPrice}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                        )
                    }
                }

                if (product.lastKnownPrice > product.price) {
                    Text(
                        "Price dropped by ₹${product.lastKnownPrice - product.price}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onAddToWatch(product) },
                        modifier = Modifier
                            .height(36.dp)
                            .width(140.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Add to ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchResultCardPreview() {
    val dummyProduct = Product(
        name = "NoiseFit Halo Smartwatch",
        platformName = "Amazon",
        priceRaw = "₹3499",
        lastKnownPrice = 4499,
        deepLink = "https://amazon.in/noisefit",
        imageURL = "https://images.unsplash.com/photo-1588776814546-93e8ed4785b8"
    )

    var isSaved by remember { mutableStateOf(false) }

    SearchResultCard(
        product = dummyProduct,
        isSaved = isSaved,
        onToggleSave = { isSaved = !isSaved },
        onAddToWatch = {}
    )
}








//
//@Composable
//fun SearchResultCard(product: Product) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 4.dp),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainer,
//            contentColor = MaterialTheme.colorScheme.onSurface
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(12.dp)
//                .fillMaxWidth()
//        ) {
//            AsyncImage(
//                model = product.imageURL,
//                contentDescription = "Product Image",
//               //error = painterResource(R.drawable.ic_broken),
//                modifier = Modifier
//                    .size(72.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(MaterialTheme.colorScheme.surfaceVariant),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//            ) {
//                Text(
//                    text = product.name,
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = "₹${product.price}",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                if (product.lastKnownPrice > product.price) {
//                    Text(
//                        text = "Dropped from ₹${product.lastKnownPrice}",
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = "Best deal on ${product.platformName}",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Row {
//                    repeat(4) {
//                        Icon(
//                            Icons.Default.Star,
//                            contentDescription = "Rating",
//                            modifier = Modifier.size(16.dp),
//                            tint = Color(0xFFFFD700)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
