package com.abra.hexmaster.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.ui.components.ArcadeButton

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
                title = { Text("HEXMASTER", fontWeight = FontWeight.Black, letterSpacing = 2.sp) },
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Master the color.\nGuess the code.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                HighScoreCard(
                    overall = highScores["overall"] ?: 0,
                    bestStreak = bestStreak,
                    easy = highScores[Difficulty.EASY.name] ?: 0,
                    medium = highScores[Difficulty.MEDIUM.name] ?: 0,
                    hard = highScores[Difficulty.HARD.name] ?: 0
                )
            }

            ArcadeButton(
                text = "PLAY",
                onClick = onPlayClick
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "BEST PERFORMANCE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("OVERALL", style = MaterialTheme.typography.labelSmall)
                    Text(overall.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("STREAK", style = MaterialTheme.typography.labelSmall)
                    Text(bestStreak.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(score.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
