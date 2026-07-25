package com.abra.hexmaster.core.scoring

import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.data.model.Difficulty
import kotlin.math.roundToInt

object ScoreCalculator {

    fun calculateRoundScore(
        difficulty: Difficulty,
        similarityPercent: Double,
        remainingTimeSeconds: Long = 0,
        totalTimeSeconds: Long = GameBalanceConfig.HARD_MODE_TIMER_SECONDS
    ): Int {
        val basePoints = GameBalanceConfig.getBasePoints(difficulty)
        val accuracyFraction = similarityPercent / 100.0
        
        val speedBonus = if (difficulty == Difficulty.HARD && totalTimeSeconds > 0) {
            (remainingTimeSeconds.toDouble() / totalTimeSeconds.toDouble()) * 0.5 + 1.0
        } else {
            1.0
        }

        return (basePoints * accuracyFraction * speedBonus).roundToInt()
    }
}
