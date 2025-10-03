package com.example.dealspy.view.utils
//
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import com.example.dealspy.data.model.Product
//import kotlinx.coroutines.launch
//import kotlin.math.roundToInt
//
//@Composable
//fun SwipeToDeleteCard(
//    product: Product,
//    onClick: () -> Unit,
//    onDelete: (Product) -> Unit,
//) {
//    val offsetX = remember { Animatable(0f) }
//    val threshold = 200f
//    val coroutineScope = rememberCoroutineScope()
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        // Background delete icon
//        Icon(
//            imageVector = Icons.Default.Delete,
//            contentDescription = "Delete",
//            tint = MaterialTheme.colorScheme.onError,
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .padding(end = 24.dp)
//        )
//
//        Card(
//            modifier = Modifier
//                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
//                .pointerInput(Unit) {
//                    detectHorizontalDragGestures(
//                        onDragEnd = {
//                            if (offsetX.value < -threshold) {
//                                onDelete(product)
//                            } else {
//                                coroutineScope.launch {
//                                    offsetX.animateTo(0f, animationSpec = tween(300))
//                                }
//                            }
//                        },
//                        onHorizontalDrag = { change, dragAmount ->
//                            change.consume()
//                            val newOffset = offsetX.value + dragAmount
//                            coroutineScope.launch {
//                                if (newOffset <= 0f) offsetX.snapTo(newOffset)
//                            }
//                        }
//                    )
//                }
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            elevation = CardDefaults.cardElevation(6.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onSurface
//            ),
//            onClick = onClick
//        ) {
//            ProductCardContent(product = product)
//        }
//    }
//}
//
//@Preview
//@Composable
//fun CardPreview(){
//
////    SwipeToDeleteCard(
////        product = {},
////        onClick = TODO()
////    ) { }
////
//}
