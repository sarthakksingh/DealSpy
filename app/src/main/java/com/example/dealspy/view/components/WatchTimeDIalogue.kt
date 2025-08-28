package com.example.dealspy.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dealspy.data.model.Product

@Composable
fun WatchTimeDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val options = listOf(
        "1 Day" to 1,
        "1 Week" to 7,
        "2 Weeks" to 14,
        "1 Month" to 30,
        "3 Months" to 90,
        "6 Months" to 180
    )
    var selected by remember { mutableIntStateOf(options.first().second) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Set Watch Time") },
        text = {
            Column {
                options.forEach { (label, days) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = days }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selected == days,
                            onClick = { selected = days }
                        )
                        Text(text = label)
                    }
                }
            }
        }
    )
}
