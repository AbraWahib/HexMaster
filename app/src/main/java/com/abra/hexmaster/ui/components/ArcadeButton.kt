package com.abra.hexmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.ui.theme.ArcadeCoral
import com.abra.hexmaster.ui.theme.ArcadeMagenta

@Composable
fun ArcadeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    useGradient: Boolean = false
) {
    val content = @Composable {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    color = Color.White
                )
            )
        }
    }

    val buttonModifier = modifier
        .fillMaxWidth()
        .height(56.dp)
        .shadow(
            elevation = if (enabled) 8.dp else 0.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = if (useGradient) ArcadeCoral else containerColor,
            spotColor = if (useGradient) ArcadeMagenta else containerColor
        )
        .clip(RoundedCornerShape(12.dp))
        .then(
            if (useGradient && enabled) {
                Modifier.background(
                    Brush.horizontalGradient(listOf(ArcadeCoral, ArcadeMagenta))
                )
            } else {
                Modifier.background(if (enabled) containerColor else containerColor.copy(alpha = 0.5f))
            }
        )
        .clickable(enabled = enabled, onClick = onClick)

    Box(modifier = buttonModifier) {
        content()
    }
}
