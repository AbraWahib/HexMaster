package com.abra.hexmaster.core.scoring

import com.abra.hexmaster.core.color.HexColor
import com.abra.hexmaster.data.model.ArrowIndicator
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.FeedbackColor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GuessEvaluatorTest {

    @Test
    fun testEvaluateEasy() {
        val answer = HexColor(255, 255, 255) // FFFFFF
        val guess = "FDFA07" // F=0, D=2, F=0, A=5, 0=F(15), 7=8
        
        val feedback = GuessEvaluator.evaluateEasy(guess, answer)
        
        // F vs F -> GREEN, NONE
        assertEquals(FeedbackColor.GREEN, feedback[0].color)
        assertEquals(ArrowIndicator.NONE, feedback[0].arrow)
        
        // D vs F -> abs(13-15)=2 -> YELLOW, UP (single)
        assertEquals(FeedbackColor.YELLOW, feedback[1].color)
        assertEquals(ArrowIndicator.UP, feedback[1].arrow)

        // A vs F -> abs(10-15)=5 -> RED, UP_UP (double)
        assertEquals(FeedbackColor.RED, feedback[3].color)
        assertEquals(ArrowIndicator.UP_UP, feedback[3].arrow)

        // 0 vs F -> abs(0-15)=15 -> RED, UP_UP
        assertEquals(FeedbackColor.RED, feedback[4].color)
        assertEquals(ArrowIndicator.UP_UP, feedback[4].arrow)
    }

    @Test
    fun testCalculateSimilarity() {
        val answer = HexColor(255, 255, 255) // FFFFFF
        val guess = HexColor(255, 255, 255)
        assertEquals(100.0, GuessEvaluator.calculateSimilarity(guess, answer), 0.01)

        val guess2 = HexColor(0, 0, 0)
        assertEquals(0.0, GuessEvaluator.calculateSimilarity(guess2, answer), 0.01)
    }

    @Test
    fun testIsPass() {
        val answer = HexColor(255, 255, 255)
        
        // Easy: pass if no RED
        assertTrue(GuessEvaluator.isPass(Difficulty.EASY, "FFFFFF", answer))
        assertTrue(GuessEvaluator.isPass(Difficulty.EASY, "FDFDFD", answer)) // All YELLOW/GREEN
        assertFalse(GuessEvaluator.isPass(Difficulty.EASY, "000000", answer)) // REDs
        
        // Medium: pass if >= 70%
        assertTrue(GuessEvaluator.isPass(Difficulty.MEDIUM, "FFFFFF", answer))
        assertFalse(GuessEvaluator.isPass(Difficulty.MEDIUM, "000000", answer))
    }

    @Test
    fun testIsPerfectMatch() {
        val answer = HexColor(255, 255, 255) // FFFFFF
        assertTrue(GuessEvaluator.isPerfectMatch("FFFFFF", answer))
        assertTrue(GuessEvaluator.isPerfectMatch("#FFFFFF", answer))
        assertFalse(GuessEvaluator.isPerfectMatch("FFFFFE", answer))
    }
}
