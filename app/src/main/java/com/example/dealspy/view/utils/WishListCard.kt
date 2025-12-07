package com.example.dealspy.view.utils

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dealspy.R
import com.example.dealspy.data.model.Product

@Composable
fun WishlistCard(
    context: Context,
    product: Product,
    onDelete: () -> Unit,
    onAddToWatchlist: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(vertical = 4.dp)
            .clickable{
                openDeepLink(url= product.deepLink, context = context)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Column(
                modifier = Modifier

                    .padding(10.dp)
            ){

                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),

                    )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.name?:"Unknown",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row {

                    product.getDiscountPercentage()?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${product.price ?: 0.0}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = { onAddToWatchlist(product) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Add to ",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter =  painterResource(id = R.drawable.shopping),
                        contentDescription = "Cart",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }


            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        painter =  painterResource(id = R.drawable.cross),
                        contentDescription = "Delete",
                        tint =Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }

            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun WishlistCardPreview() {
    val context = LocalContext.current
    val sampleProduct = Product(
        id = "1",
        name = "Sample Wishlist Product",
        brand = "Sample Brand",
        platformName = "Amazon",
        price = 1999.0,
        lastKnownPrice = 2499.0,
        deepLink = "https://www.example.com",
        imageUrl = "https://via.placeholder.com/300"
    )

    WishlistCard(
        context= context,
        product = sampleProduct,
        onDelete = {},
        onAddToWatchlist = {}

    )
}
