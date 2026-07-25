package com.abra.hexmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.data.model.DigitFeedback
import com.abra.hexmaster.data.model.FeedbackColor

@Composable
fun DigitFeedbackRow(
    guessHex: String,
    feedback: List<DigitFeedback>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        feedback.forEachIndexed { index, item ->
            val char = guessHex.getOrNull(index)?.toString() ?: ""
            DigitFeedbackBox(char, item)
        }
    }
}

@Composable
private fun DigitFeedbackBox(
    char: String,
    feedback: DigitFeedback
) {
    val backgroundColor = when (feedback.color) {
        FeedbackColor.GREEN -> Color(0xFF4CAF50)
        FeedbackColor.YELLOW -> Color(0xFFFFC107)
        FeedbackColor.RED -> Color(0xFFF44336)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(width = 48.dp, height = 80.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        Text(
            text = char.uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        ArrowIndicatorIcon(indicator = feedback.arrow, tint = Color.White)
    }
}
