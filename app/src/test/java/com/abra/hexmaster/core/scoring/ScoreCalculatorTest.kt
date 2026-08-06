package com.abra.hexmaster.core.scoring

import com.abra.hexmaster.data.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreCalculatorTest {

    @Test
    fun testCalculateRoundScore() {
        // Easy, 100% similarity -> 100 points
        assertEquals(100, ScoreCalculator.calculateRoundScore(Difficulty.EASY, 100.0))
        
        // Medium, 50% similarity -> 150 * 0.5 = 75 points
        assertEquals(75, ScoreCalculator.calculateRoundScore(Difficulty.MEDIUM, 50.0))
        
        // Hard, 100% similarity, 10s remaining (out of 20s) -> 200 * 1.0 * (0.5 * 0.5 + 1.0) = 200 * 1.25 = 250
        assertEquals(250, ScoreCalculator.calculateRoundScore(Difficulty.HARD, 100.0, 10, 20))

        // Hard, 100% similarity, 0s remaining -> 200 * 1.0 * 1.0 = 200
        assertEquals(200, ScoreCalculator.calculateRoundScore(Difficulty.HARD, 100.0, 0, 20))
    }
}
