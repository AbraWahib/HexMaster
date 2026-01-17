package com.abra.HexMaster.presentation.easy_level.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.ui.theme.HexMasterTheme

@Composable
fun DigitCheckingSection(modifier: Modifier = Modifier) {
    Column (modifier.fillMaxWidth()){
        Text(
            text = "Previous Guesses Feedback".uppercase(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(6) {
                DigitCheckingCard()
            }
        }
    }
}

@Preview
@Composable
private fun DigitSectionPrev() {
    HexMasterTheme {
        Surface {
            DigitCheckingSection()
        }
    }
}