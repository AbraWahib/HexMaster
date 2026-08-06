package com.abra.hexmaster.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.ui.theme.ArcadeCoral
import com.abra.hexmaster.ui.theme.ArcadeCyan
import com.abra.hexmaster.ui.theme.ArcadeMagenta

@Composable
fun ArcadeHeroVisual(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    
    val color1 by infiniteTransition.animateColor(
        initialValue = ArcadeCoral,
        targetValue = ArcadeMagenta,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color1"
    )
    
    val color2 by infiniteTransition.animateColor(
        initialValue = ArcadeCyan,
        targetValue = ArcadeCoral,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color2"
    )

    Box(modifier = modifier.size(240.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    0.0f to color1.copy(alpha = 0.4f),
                    1.0f to Color.Transparent,
                    center = center,
                    radius = size.minDimension / 1.2f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    0.0f to color2.copy(alpha = 0.3f),
                    1.0f to Color.Transparent,
                    center = center.copy(x = center.x * 0.8f, y = center.y * 1.2f),
                    radius = size.minDimension / 1.5f
                )
            )
        }
    }
}
