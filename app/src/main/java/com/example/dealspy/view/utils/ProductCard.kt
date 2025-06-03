package com.example.dealspy.view.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.dealspy.data.model.Product

@Composable
fun ProductCardContent(product: Product) {

    Row(modifier = Modifier.padding(12.dp)) {
        Image(
            painter = rememberAsyncImagePainter(product.imageURL),
            contentDescription = product.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.Bold)
            Text("₹${product.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Best deal: ${product.platformName}", style = MaterialTheme.typography.bodySmall)
        }

        if ( product.price < product.lastKnownPrice) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Price Dropped",
                tint = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}
