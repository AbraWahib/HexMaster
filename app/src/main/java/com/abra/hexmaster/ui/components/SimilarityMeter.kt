package com.abra.hexmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.core.color.HexColor
import com.abra.hexmaster.core.color.HexColorUtils
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.DigitFeedback
import com.abra.hexmaster.data.model.FeedbackColor
import com.abra.hexmaster.ui.theme.HexMasterTheme
import kotlin.math.roundToInt

@Composable
fun SimilarityMeter(
    similarityPercent: Double,
    guessHex: String,
    feedback: List<DigitFeedback>,
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val color = when {
        similarityPercent >= 90 -> Color(0xFF4CAF50)
        similarityPercent >= 70 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    val guessRgb = try {
        HexColorUtils.hexToRgb(guessHex)
    } catch (e: Exception) {
        Triple(0, 0, 0)
    }
    val guessColor = Color(guessRgb.first, guessRgb.second, guessRgb.third, 255)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .size(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = guessColor)
        )
        if (difficulty == Difficulty.EASY) {
            Row(
                modifier = Modifier.fillMaxHeight().weight(1f),


                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                feedback.forEachIndexed { index, item ->
                    val char = guessHex.getOrNull(index)?.toString() ?: ""
                    DigitFeedbackBox(
                        char, item, modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }
            }
        } else {
            Text(
                "#$guessHex",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 4.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${similarityPercent.roundToInt()}%",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = color
            )
        }
    }
}

/* Column(
     modifier = modifier.fillMaxWidth(),
     horizontalAlignment = Alignment.CenterHorizontally
 ) {
     Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceEvenly,
         verticalAlignment = Alignment.CenterVertically
     ) {
         Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Text("TARGET", style = MaterialTheme.typography.labelSmall)
             Box(
                 modifier = Modifier
                     .size(32.dp)
                     .clip(CircleShape)
                     .background(targetComposeColor)
                     .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
             )
             Text(
                 "#${targetColor.hex}",
                 style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
             )
         }

         Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Text(
                 text = "${similarityPercent.roundToInt()}%",
                 style = MaterialTheme.typography.titleLarge.copy(
                     fontWeight = FontWeight.Bold,
                     fontSize = 24.sp
                 ),
                 color = color
             )
             Text("SIMILARITY", style = MaterialTheme.typography.labelSmall)
         }

         Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Text("GUESS", style = MaterialTheme.typography.labelSmall)
             Box(
                 modifier = Modifier
                     .size(32.dp)
                     .clip(CircleShape)
                     .background(guessColor)
                     .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
             )
             Text(
                 "#$guessHex",
                 style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
             )
         }
     }

     Spacer(modifier = Modifier.height(8.dp))

     LinearProgressIndicator(
         progress = { (similarityPercent / 100.0).toFloat() },
         modifier = Modifier
             .fillMaxWidth(0.8f)
             .height(8.dp)
             .clip(CircleShape),
         color = color,
         trackColor = MaterialTheme.colorScheme.surfaceVariant
     )
 } */
@Composable
private fun DigitFeedbackBox(
    char: String,
    feedback: DigitFeedback,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (feedback.color) {
        FeedbackColor.GREEN -> Color(0xFF4CAF50)
        FeedbackColor.YELLOW -> Color(0xFFFFC107)
        FeedbackColor.RED -> Color(0xFFF44336)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        Text(
            text = char.uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        ArrowIndicatorIcon(indicator = feedback.arrow, tint = Color.White)
    }
}


