package com.abra.HexMaster.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.R
import com.abra.HexMaster.presentation.home.components.Level
import com.abra.HexMaster.presentation.home.components.LevelButton

@Composable
fun HomeScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(96.dp))
            Icon(
                painter = painterResource(R.drawable.color_swatch),
                contentDescription = "logo",
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.main_screen_label),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .6f
                    )
                )
            )
            Spacer(Modifier.height(32.dp))
            LevelButton(Level.Easy, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            LevelButton(Level.Medium, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            LevelButton(Level.Hard, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.message_question),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.how_to_play),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = .6f
                        )
                    )
                )
            }
        }
    }
}
