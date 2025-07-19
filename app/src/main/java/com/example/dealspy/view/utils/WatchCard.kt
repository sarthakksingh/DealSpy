package com.example.dealspy.view.utils

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.dealspy.data.model.UiProduct
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@SuppressLint("DefaultLocale")
@Composable
fun ProductCard(uiProduct: UiProduct) {
    val context = LocalContext.current
    var remainingTime by remember { mutableLongStateOf(uiProduct.timeLeftMillis) }

    // Timer logic
    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1000L
        }
    }

    val hours = TimeUnit.MILLISECONDS.toHours(remainingTime)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60
    val timerText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Load product image
                AsyncImage(
                    model = uiProduct.product.imageURL,
                    contentDescription = uiProduct.product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiProduct.brand,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = uiProduct.product.name,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${uiProduct.product.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "₹${uiProduct.product.lastKnownPrice}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${uiProduct.discountPercent}% OFF",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Red)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "🔻 Price dropped by ₹${uiProduct.product.lastKnownPrice - uiProduct.product.price}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Red)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.85f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "⏳ $timerText", color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, uiProduct.product.deepLink.toUri())
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("BUY NOW", color = Color.White)
                }
            }

            // Remove (X) icon top-right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.White, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✕", fontSize = 12.sp)
                }
            }
        }
    }
}
