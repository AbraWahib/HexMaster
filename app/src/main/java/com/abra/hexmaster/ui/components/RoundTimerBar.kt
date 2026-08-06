package com.abra.hexmaster.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.core.GameBalanceConfig

@Composable
fun RoundTimerBar(
    remainingSeconds: Long,
    totalSeconds: Long = GameBalanceConfig.HARD_MODE_TIMER_SECONDS,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val color = when {
        progress > 0.5f -> Color(0xFF4CAF50)
        progress > 0.2f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = "${remainingSeconds}s",
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
