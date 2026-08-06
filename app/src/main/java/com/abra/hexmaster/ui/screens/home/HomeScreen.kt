package com.abra.hexmaster.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.R
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.ui.components.ArcadeButton
import com.abra.hexmaster.ui.components.ArcadeHeroVisual

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPlayClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val highScores by viewModel.highScores.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()
    var showHowToPlay by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.hexmaster),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                            .width(160.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { showHowToPlay = true }) {
                        Icon(Icons.Default.Info, contentDescription = "How to Play")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Guess the code. Master the color.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            HighScoreCard(
                overall = highScores["overall"] ?: 0,
                bestStreak = bestStreak,
                easy = highScores[Difficulty.EASY.name] ?: 0,
                medium = highScores[Difficulty.MEDIUM.name] ?: 0,
                hard = highScores[Difficulty.HARD.name] ?: 0
            )
            Spacer(Modifier.weight(1f))
            ArcadeHeroVisual()
            Spacer(Modifier.weight(1f))
            ArcadeButton(
                text = "PLAY",
                onClick = onPlayClick,
                useGradient = true
            )
        }

    }

    if (showHowToPlay) {
        HowToPlayDialog(onDismiss = { showHowToPlay = false })
    }
}

@Composable
fun HowToPlayDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        title = {
            Text(
                "HOW TO PLAY",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("• A random color is shown.")
                Text("• Guess its 6-digit hex code.")
                Text("• You have 6 tries per round.")
                Text("• Lose all tries → Lose a life.")
                Text("• 3 lives total. Good luck!")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("GOT IT", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun HighScoreCard(
    overall: Int,
    bestStreak: Int,
    easy: Int,
    medium: Int,
    hard: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "BEST PERFORMANCE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("OVERALL", style = MaterialTheme.typography.labelSmall)
                    Text(
                        overall.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("STREAK", style = MaterialTheme.typography.labelSmall)
                    Text(
                        bestStreak.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            HighScoreRow("EASY", easy)
            HighScoreRow("MEDIUM", medium)
            HighScoreRow("HARD", hard)
        }
    }
}

@Composable
fun HighScoreRow(label: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            score.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
