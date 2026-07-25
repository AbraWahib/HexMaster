package com.abra.hexmaster.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.ui.components.*

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

    Scaffold(
        topBar = {
            GameTopBar(
                score = uiState.score,
                lives = uiState.lives,
                round = uiState.roundIndex,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.difficulty == Difficulty.HARD) {
                RoundTimerBar(remainingSeconds = uiState.remainingTimeSeconds)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
                items(uiState.previousGuesses) { result ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (uiState.difficulty == Difficulty.EASY) {
                                DigitFeedbackRow(guessHex = result.guessHex, feedback = result.feedback)
                            } else {
                                SimilarityMeter(similarityPercent = result.similarity)
                            }
                        }
                    }
                }
            }

            // Input Area
            Spacer(modifier = Modifier.height(16.dp))

            if (!isRoundFinished) {
                HexOtpInputField(
                    value = inputText,
                    onValueChange = viewModel::onInputChanged,
                    isError = isError,
                    onShakeComplete = viewModel::onShakeComplete
                )

                Spacer(modifier = Modifier.height(16.dp))

                ArcadeButton(
                    text = "SUBMIT",
                    onClick = viewModel::submitGuess,
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
                    onClick = viewModel::nextRound
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    score: Int,
    lives: Int,
    round: Int,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
            ) {
                Column {
                    Text("SCORE", style = MaterialTheme.typography.labelSmall)
                    Text(score.toString(), fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ROUND", style = MaterialTheme.typography.labelSmall)
                    Text(round.toString(), fontWeight = FontWeight.Bold)
                }
                LivesDisplay(lives = lives)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}
