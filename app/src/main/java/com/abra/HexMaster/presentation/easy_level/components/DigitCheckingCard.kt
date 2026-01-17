package com.abra.HexMaster.presentation.easy_level.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.ui.theme.HexMasterTheme

@Composable
fun DigitCheckingCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(56.dp)
            .wrapContentHeight()
            .padding(8.dp, 0.dp, 8.dp, 8.dp),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp,Color(0xFF7986CB))
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(Color(0xFF7986CB))
            )
            Spacer(Modifier.height(4.dp))
            Text("9", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Preview
@Composable
private fun DigitCheckingPrev() {
    HexMasterTheme {
        Surface {
            Box{ DigitCheckingCard() }
        }
    }
}