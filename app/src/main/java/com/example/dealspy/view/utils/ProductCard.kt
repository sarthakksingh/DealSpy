package com.example.dealspy.view.utils
//
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import com.example.dealspy.data.model.Product
//
//@Composable
//fun ProductCardContent(product: Product) {
//    Row(
//        modifier = Modifier
//            .padding(12.dp)
//            .fillMaxWidth()
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(product.imageURL),
//            contentDescription = product.name,
//            modifier = Modifier
//                .size(64.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = product.name,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(
//                text = "₹${product.price}",
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.primary
//            )
//
//            Spacer(modifier = Modifier.height(2.dp))
//
//            Text(
//                text = "Best deal: ${product.platformName}",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//
//        if (product.price < product.lastKnownPrice) {
//            Icon(
//                imageVector = Icons.Default.KeyboardArrowDown,
//                contentDescription = "Price Dropped",
//                tint = MaterialTheme.colorScheme.error,
//                modifier = Modifier
//                    .size(24.dp)
//                    .padding(start = 8.dp)
//            )
//        }
//    }
//}
