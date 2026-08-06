package com.abra.hexmaster.ui.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.GameUiState
import com.abra.hexmaster.data.model.RoundResult
import com.abra.hexmaster.ui.components.ArcadeButton
import com.abra.hexmaster.ui.components.ColorSwatch
import com.abra.hexmaster.ui.components.DigitFeedbackRow
import com.abra.hexmaster.ui.components.GameTopBar
import com.abra.hexmaster.ui.components.GuessFeedbackCard
import com.abra.hexmaster.ui.components.HexOtpInputField
import com.abra.hexmaster.ui.components.RoundTimerBar
import com.abra.hexmaster.ui.components.SimilarityMeter
import com.abra.hexmaster.ui.components.StreakDisplay
import com.abra.hexmaster.ui.theme.HexMasterTheme

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameOver: (Int, Int, String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isError by viewModel.isInputError.collectAsState()
    val listState = rememberLazyListState()
    var showQuitDialog by remember { mutableStateOf(false) }

    val isRoundFinished = uiState.lastRoundResult?.isPass == true ||
            uiState.triesUsed >= GameBalanceConfig.MAX_TRIES_PER_ROUND

    LaunchedEffect(uiState.isGameOver) {
        if (uiState.isGameOver) {
            onGameOver(uiState.score, uiState.bestStreakInSession, uiState.difficulty.name)
        }
    }

    // Auto-scroll to latest guess
    LaunchedEffect(uiState.previousGuesses.size) {
        if (uiState.previousGuesses.isNotEmpty()) {
            listState.animateScrollToItem(uiState.previousGuesses.size - 1)
        }
    }

    BackHandler(enabled = !uiState.isGameOver) {
        showQuitDialog = true
    }

    GameScreenContent(
        uiState = uiState,
        isError = isError,
        listState = listState,
        isRoundFinished = isRoundFinished,
        inputText = inputText,
        onBackRequest = { showQuitDialog = true },
        onEvent = viewModel::onEvent
    )

    if (showQuitDialog) {
        QuitConfirmationDialog(
            onConfirm = {
                showQuitDialog = false
                viewModel.onEvent(GameEvent.OnQuitConfirmed)
            },
            onDismiss = { showQuitDialog = false }
        )
    }
}

@Composable
fun GameScreenContent(
    uiState: GameUiState,
    isError: Boolean,
    listState: LazyListState,
    isRoundFinished: Boolean,
    inputText: String,
    onBackRequest: () -> Unit,
    onEvent: (GameEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            GameTopBar(
                score = uiState.score,
                lives = uiState.lives,
                round = uiState.roundIndex,
                onBack = onBackRequest
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.difficulty == Difficulty.HARD) {
                RoundTimerBar(remainingSeconds = uiState.remainingTimeSeconds)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StreakDisplay(streak = uiState.currentStreak)

                Column(horizontalAlignment = Alignment.End) {
                    Text("TRY", style = MaterialTheme.typography.labelSmall)
                    Text(
                        "${uiState.triesUsed}/${GameBalanceConfig.MAX_TRIES_PER_ROUND}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            ColorSwatch(color = uiState.targetColor, modifier = Modifier.padding(bottom = 16.dp))

            // Scrollable History
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Previous Attempts",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Previous Attempts",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                items(uiState.previousGuesses) { result ->
                    GuessFeedbackCard(result = result, gameDifficulty = uiState.difficulty)
                }
            }

            // Input Area
            Spacer(modifier = Modifier.height(16.dp))

            if (!isRoundFinished) {
                HexOtpInputField(
                    value = inputText,
                    onValueChange = { onEvent(GameEvent.OnInputChanged(it)) },
                    isError = isError,
                    onShakeComplete = { onEvent(GameEvent.OnShakeComplete) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ArcadeButton(
                    text = "SUBMIT",
                    onClick = { onEvent(GameEvent.SubmitGuess) },
                    enabled = inputText.length == 6
                )
            } else {
                val result = uiState.lastRoundResult!!
                Text(
                    text = if (result.isPass) "CORRECT!" else "OUT OF TRIES!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (result.isPass) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ArcadeButton(
                    text = "NEXT",
                    onClick = { onEvent(GameEvent.NextRound) }
                )
            }
        }
    }
}

@Composable
fun QuitConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("QUIT GAME?") },
        text = { Text("Are you sure you want to quit? Your current score will be saved.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("QUIT", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("STAY")
            }
        }
    )
}
