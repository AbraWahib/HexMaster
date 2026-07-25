package com.abra.hexmaster.data.model

import com.abra.hexmaster.core.color.HexColor

data class GameUiState(
    val difficulty: Difficulty = Difficulty.EASY,
    val targetColor: HexColor = HexColor(0, 0, 0),
    val lives: Int = 3,
    val score: Int = 0,
    val currentStreak: Int = 0,
    val bestStreakInSession: Int = 0,
    val roundIndex: Int = 1,
    val triesUsed: Int = 0,
    val isGameOver: Boolean = false,
    val lastRoundResult: RoundResult? = null,
    val previousGuesses: List<RoundResult> = emptyList(),
    val remainingTimeSeconds: Long = 0
)
