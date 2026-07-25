package com.abra.hexmaster.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LivesDisplay(
    lives: Int,
    maxLives: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        repeat(maxLives) { index ->
            val isFilled = index < lives
            Icon(
                imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFilled) "Life" else "Lost Life",
                tint = if (isFilled) Color.Red else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(32.dp)
                    .padding(horizontal = 2.dp)
            )
        }
    }
}
