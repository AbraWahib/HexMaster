package com.abra.hexmaster.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.RoundResult

@Composable
fun GuessFeedbackCard(
    result: RoundResult,
    gameDifficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SimilarityMeter(
                similarityPercent = result.similarity,
                guessHex = result.guessHex,
                feedback = result.feedback,
                difficulty = gameDifficulty
            )

        }
    }
}