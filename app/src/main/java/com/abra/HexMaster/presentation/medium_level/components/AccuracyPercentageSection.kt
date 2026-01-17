package com.abra.HexMaster.presentation.medium_level.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.ui.theme.HexMasterTheme

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun AccuracyPercentageSection(percentage: Int, color: Int, modifier: Modifier = Modifier) {
    Card(
        modifier
            .height(96.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
    ) {
        Box(
            Modifier.background(Color(0xFFAED581)).fillMaxSize()
        ) {
            BrushPainter(
                Brush.verticalGradient(listOf(Color.Transparent,Color.Black))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.White)
                    .alpha(.7f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("#${color.toHexString()}", style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "$percentage %",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp)
            )
        }
        Box(Modifier.fillMaxSize()){
            BrushPainter(
                Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black)
                )
            )
        }
    }
}

@Preview
@Composable
private fun AccuracyPercentagePrev() {
    HexMasterTheme {
        Surface { AccuracyPercentageSection(97, 111111) }
    }
}