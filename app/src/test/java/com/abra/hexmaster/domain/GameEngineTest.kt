package com.abra.hexmaster.domain

import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.core.color.ColorGenerator
import com.abra.hexmaster.data.model.Difficulty
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class GameEngineTest {

    private val seededRandom = Random(42)
    private val colorGenerator = ColorGenerator(seededRandom)

    @Test
    fun testInitialState() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.EASY)
        val state = engine.state.value
        
        assertEquals(Difficulty.EASY, state.difficulty)
        assertEquals(GameBalanceConfig.STARTING_LIVES, state.lives)
        assertEquals(0, state.score)
        assertEquals(0, state.currentStreak)
        assertEquals(0, state.triesUsed)
        assertFalse(state.isGameOver)
    }

    @Test
    fun testSubmitCorrectGuess() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.EASY)
        val targetHex = engine.state.value.targetColor.hex
        
        engine.submitGuess(targetHex)
        
        val state = engine.state.value
        assertTrue(state.score > 0)
        assertEquals(1, state.currentStreak)
        assertEquals(1, state.triesUsed)
        assertEquals(GameBalanceConfig.STARTING_LIVES, state.lives)
        assertTrue(state.lastRoundResult?.isPass == true)
    }

    @Test
    fun testSubmitIncorrectGuessWithinTries() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.MEDIUM)
        
        // Assume "000000" is incorrect for the seeded random target
        engine.submitGuess("000000")
        
        val state = engine.state.value
        assertEquals(1, state.triesUsed)
        assertEquals(1, state.previousGuesses.size)
        assertEquals(GameBalanceConfig.STARTING_LIVES, state.lives) // Life NOT lost yet
        assertFalse(state.lastRoundResult?.isPass == true)
    }

    @Test
    fun testHistoryAccumulation() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.MEDIUM)
        
        engine.submitGuess("000000")
        engine.submitGuess("111111")
        
        val state = engine.state.value
        assertEquals(2, state.triesUsed)
        assertEquals(2, state.previousGuesses.size)
        assertEquals("000000", state.previousGuesses[0].guessHex)
        assertEquals("111111", state.previousGuesses[1].guessHex)
    }

    @Test
    fun testLoseLifeAfterMaxTries() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.MEDIUM)
        
        repeat(GameBalanceConfig.MAX_TRIES_PER_ROUND) {
            engine.submitGuess("000000")
        }
        
        val state = engine.state.value
        assertEquals(GameBalanceConfig.MAX_TRIES_PER_ROUND, state.triesUsed)
        assertEquals(GameBalanceConfig.STARTING_LIVES - 1, state.lives) // Life lost now
        assertEquals(0, state.currentStreak)
    }

    @Test
    fun testGameOver() = runBlocking {
        val engine = GameEngine(colorGenerator, Difficulty.MEDIUM)
        
        // Lose all lives
        repeat(GameBalanceConfig.STARTING_LIVES) {
            repeat(GameBalanceConfig.MAX_TRIES_PER_ROUND) {
                engine.submitGuess("000000")
            }
            if (it < GameBalanceConfig.STARTING_LIVES - 1) {
                engine.nextRound()
            }
        }
        
        assertTrue(engine.state.value.isGameOver)
    }
}
