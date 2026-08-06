package com.abra.hexmaster.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.data.model.ArrowIndicator

@Composable
fun ArrowIndicatorIcon(
    indicator: ArrowIndicator,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    val size = 20.dp
    when (indicator) {
        ArrowIndicator.NONE -> {}
        ArrowIndicator.UP -> {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = tint, modifier = modifier.size(size))
        }
        ArrowIndicator.DOWN -> {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = tint, modifier = modifier.size(size))
        }
        ArrowIndicator.UP_UP -> {
            Row(modifier = modifier) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Double Up", tint = tint, modifier = Modifier.size(size))
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = tint, modifier = Modifier.size(size))
            }
        }
        ArrowIndicator.DOWN_DOWN -> {
            Row(modifier = modifier) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Double Down", tint = tint, modifier = Modifier.size(size))
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = tint, modifier = Modifier.size(size))
            }
        }
    }
}
