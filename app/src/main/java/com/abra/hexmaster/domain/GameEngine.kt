package com.abra.hexmaster.domain

import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.core.color.ColorGenerator
import com.abra.hexmaster.core.color.HexColor
import com.abra.hexmaster.core.color.HexColorUtils
import com.abra.hexmaster.core.scoring.GuessEvaluator
import com.abra.hexmaster.core.scoring.ScoreCalculator
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.GameUiState
import com.abra.hexmaster.data.model.RoundResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameEngine(
    private val colorGenerator: ColorGenerator,
    private val initialDifficulty: Difficulty
) {
    private val _state = MutableStateFlow(
        GameUiState(
            difficulty = initialDifficulty,
            targetColor = colorGenerator.generateRandomColor(),
            lives = GameBalanceConfig.STARTING_LIVES,
            remainingTimeSeconds = if (initialDifficulty == Difficulty.HARD) 
                GameBalanceConfig.HARD_MODE_TIMER_SECONDS else 0
        )
    )
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    fun submitGuess(guessHex: String, remainingTimeSeconds: Long = 0) {
        if (_state.value.isGameOver) return
        if (!HexColorUtils.isValidHex(guessHex)) return

        val normalizedGuess = guessHex.removePrefix("#").uppercase()
        val targetColor = _state.value.targetColor
        
        val isPass = GuessEvaluator.isPass(_state.value.difficulty, normalizedGuess, targetColor)
        val similarity = if (_state.value.difficulty == Difficulty.EASY) {
            val guessRgb = HexColorUtils.hexToRgb(normalizedGuess)
            GuessEvaluator.calculateSimilarity(HexColor(guessRgb.first, guessRgb.second, guessRgb.third), targetColor)
        } else {
            val guessRgb = HexColorUtils.hexToRgb(normalizedGuess)
            GuessEvaluator.calculateSimilarity(HexColor(guessRgb.first, guessRgb.second, guessRgb.third), targetColor)
        }

        val feedback = if (_state.value.difficulty == Difficulty.EASY) {
            GuessEvaluator.evaluateEasy(normalizedGuess, targetColor)
        } else emptyList()

        _state.update { currentState ->
            val newTriesUsed = currentState.triesUsed + 1
            
            // Round ends if guess is correct OR tries are exhausted
            val roundEnds = isPass || newTriesUsed >= GameBalanceConfig.MAX_TRIES_PER_ROUND
            
            if (roundEnds) {
                val roundScore = if (isPass) {
                    ScoreCalculator.calculateRoundScore(
                        currentState.difficulty,
                        similarity,
                        remainingTimeSeconds
                    )
                } else 0

                val result = RoundResult(
                    guessHex = normalizedGuess,
                    answerColor = targetColor,
                    isPass = isPass,
                    similarity = similarity,
                    score = roundScore,
                    feedback = feedback
                )

                val newLives = if (isPass) currentState.lives else currentState.lives - 1
                val newStreak = if (isPass) currentState.currentStreak + 1 else 0
                val isGameOver = newLives <= 0

                currentState.copy(
                    triesUsed = newTriesUsed,
                    lives = newLives,
                    score = currentState.score + roundScore,
                    currentStreak = newStreak,
                    bestStreakInSession = maxOf(currentState.bestStreakInSession, newStreak),
                    isGameOver = isGameOver,
                    lastRoundResult = result,
                    previousGuesses = currentState.previousGuesses + result
                )
            } else {
                // Wrong guess, but tries remaining
                val result = RoundResult(
                    guessHex = normalizedGuess,
                    answerColor = targetColor,
                    isPass = false,
                    similarity = similarity,
                    score = 0,
                    feedback = feedback
                )
                currentState.copy(
                    triesUsed = newTriesUsed,
                    lastRoundResult = result,
                    previousGuesses = currentState.previousGuesses + result
                )
            }
        }
    }

    fun nextRound() {
        if (_state.value.isGameOver) return
        
        _state.update { currentState ->
            currentState.copy(
                targetColor = colorGenerator.generateRandomColor(),
                roundIndex = currentState.roundIndex + 1,
                triesUsed = 0,
                lastRoundResult = null,
                previousGuesses = emptyList(),
                remainingTimeSeconds = if (currentState.difficulty == Difficulty.HARD) 
                    GameBalanceConfig.HARD_MODE_TIMER_SECONDS else 0
            )
        }
    }

    fun onTick() {
        if (_state.value.difficulty == Difficulty.HARD && _state.value.remainingTimeSeconds > 0) {
            _state.update { it.copy(remainingTimeSeconds = it.remainingTimeSeconds - 1) }
        }
    }

    fun reset(difficulty: Difficulty = _state.value.difficulty) {
        _state.value = GameUiState(
            difficulty = difficulty,
            targetColor = colorGenerator.generateRandomColor(),
            lives = GameBalanceConfig.STARTING_LIVES,
            triesUsed = 0,
            previousGuesses = emptyList(),
            remainingTimeSeconds = if (difficulty == Difficulty.HARD) 
                GameBalanceConfig.HARD_MODE_TIMER_SECONDS else 0
        )
    }
}
