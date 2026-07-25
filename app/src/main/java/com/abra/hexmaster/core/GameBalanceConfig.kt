package com.abra.hexmaster.core

import com.abra.hexmaster.data.model.Difficulty

object GameBalanceConfig {
    const val STARTING_LIVES = 3
    const val MAX_TRIES_PER_ROUND = 6
    
    const val PASS_THRESHOLD_MEDIUM_HARD = 70.0
    
    const val HARD_MODE_TIMER_SECONDS = 30L

    fun getBasePoints(difficulty: Difficulty): Int {
        return when (difficulty) {
            Difficulty.EASY -> 100
            Difficulty.MEDIUM -> 150
            Difficulty.HARD -> 200
        }
    }
}
