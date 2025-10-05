package com.example.dealspy.view.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
fun ShimmerDealCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth(if (it == 0) 0.8f else if (it == 1) 0.6f else 0.4f)
                                .clip(RoundedCornerShape(10.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    } else {
        contentAfterLoading()
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
fun ShimmerHorizontalCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Card(
            modifier = Modifier
                .width(240.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth(0.7f)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    } else {
        contentAfterLoading()
    }
}

//Search Screen Card Shimmer
@Composable
fun ShimmerSearchResultCard(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth(0.5f)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(50))
                                    .shimmerEffect()
                                    .padding(end = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    } else {
        contentAfterLoading()
    }
}


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val trasition = rememberInfiniteTransition()
    val startOffsetX by trasition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}