package com.abra.hexmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.core.color.HexColor

@Composable
fun ColorSwatch(
    color: HexColor,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(.9f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(color.r, color.g, color.b, 255))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .padding(4.dp)
            .border(2.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    )
}
