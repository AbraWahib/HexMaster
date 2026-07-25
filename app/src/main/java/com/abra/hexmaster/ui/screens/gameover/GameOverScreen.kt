package com.abra.hexmaster.ui.screens.gameover

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.hexmaster.ui.components.ArcadeButton

@Composable
fun GameOverScreen(
    score: Int,
    streak: Int,
    difficulty: String,
    onRetrySame: () -> Unit,
    onRetryChange: () -> Unit,
    onHome: () -> Unit
) {
    var showRetryDialog by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GAME OVER",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("FINAL SCORE", style = MaterialTheme.typography.labelMedium)
            Text(
                score.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("BEST STREAK", style = MaterialTheme.typography.labelMedium)
            Text(
                streak.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            ArcadeButton(
                text = "RETRY",
                onClick = { showRetryDialog = true }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("HOME", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showRetryDialog) {
        AlertDialog(
            onDismissRequest = { showRetryDialog = false },
            title = { Text("RETRY") },
            text = { Text("Do you want to play again with the same difficulty or change it?") },
            confirmButton = {
                TextButton(onClick = {
                    showRetryDialog = false
                    onRetrySame()
                }) {
                    Text("SAME DIFFICULTY")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRetryDialog = false
                    onRetryChange()
                }) {
                    Text("CHANGE")
                }
            }
        )
    }
}
