package com.example.dealspy.view.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@Composable
fun ShimmerWatchCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isLoading) {
        contentAfterLoading()
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Image placeholder (same as WatchCard)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Title (2 lines)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Platform name
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Row: lastKnownPrice + discount
                Row {
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Current price
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // BUY NOW button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect()
                )
            }

            // Top end delete icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect()
                )
            }
        }
    }
}


@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = LinearEasing),
            RepeatMode.Restart
        ), label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Platform name placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Discount placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }

            // Button placeholder
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(brush)
            )
        }
    }
}



@Composable
fun ShimmerWishlistCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isLoading) {
        contentAfterLoading()
        return
    }

    Card(
        modifier = modifier
            .width(150.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                // Image placeholder (same as WishlistCard)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product name (1 line)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Discount percentage
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Current price
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // "Add to Watchlist" button with icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect()
                )
            }

            // Top-end delete icon placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ShimmerSearchResultCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isLoading) {
        contentAfterLoading()
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
            Row(modifier = Modifier.padding(12.dp)) {
                // Image placeholder
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Name
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Platform
                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Price row
                    Row {
                        Box(
                            modifier = Modifier
                                .height(18.dp)
                                .width(70.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .height(14.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Discount
                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Bookmark
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Button
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(140.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect()
                )
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2f * size.width,
        targetValue =  2f * size.width,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val colors = listOf(
        Color.LightGray.copy(alpha = 0.4f),
        Color.White.copy(alpha = 0.7f),
        Color.LightGray.copy(alpha = 0.4f),
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(startOffsetX, 0f),
        end   = Offset(startOffsetX + size.width, size.height.toFloat())
    )

    onGloballyPositioned { size = it.size }
        .background(brush)
}
