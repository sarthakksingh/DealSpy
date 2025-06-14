package com.example.dealspy.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


    @Composable
    fun PrimaryButton(
        modifier: Modifier = Modifier,
        text: String,
        shape: Shape,
        colors: ButtonColors,
        onClick: () -> Unit = {},
        enabled: Boolean = true
    ) {
        Button(
            shape = shape,
            colors = colors,
            modifier = modifier,
            onClick = onClick,
            enabled = enabled
        ) {
            Text(
                text = text,
                maxLines = 2,
                softWrap = true,
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,

            )
        }
    }
