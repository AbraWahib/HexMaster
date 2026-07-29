package com.abra.hexmaster.core.scoring

import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.core.color.HexColor
import com.abra.hexmaster.core.color.HexColorUtils
import com.abra.hexmaster.data.model.ArrowIndicator
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.DigitFeedback
import com.abra.hexmaster.data.model.FeedbackColor
import kotlin.math.abs

object GuessEvaluator {

    fun evaluateEasy(guessHex: String, answerColor: HexColor): List<DigitFeedback> {
        val answerHex = answerColor.hex
        val feedback = mutableListOf<DigitFeedback>()

        for (i in 0 until 6) {
            val guessDigit = guessHex[i].toString().toInt(16)
            val answerDigit = answerHex[i].toString().toInt(16)
            val diff = abs(guessDigit - answerDigit)

            val color = when {
                diff == 0 -> FeedbackColor.GREEN
                diff in 1..2 -> FeedbackColor.YELLOW
                else -> FeedbackColor.RED
            }

            val arrow = when (diff) {
                0 -> ArrowIndicator.NONE
                in 1..2 -> if (guessDigit < answerDigit) ArrowIndicator.UP else ArrowIndicator.DOWN
                else -> if (guessDigit < answerDigit) ArrowIndicator.UP_UP else ArrowIndicator.DOWN_DOWN
            }

            feedback.add(DigitFeedback(color, arrow))
        }
        return feedback
    }

    fun calculateSimilarity(guessColor: HexColor, answerColor: HexColor): Double {
        val channelDiff = abs(guessColor.r - answerColor.r) +
                abs(guessColor.g - answerColor.g) +
                abs(guessColor.b - answerColor.b)
        return 100.0 - (channelDiff / 765.0 * 100.0)
    }

    fun isPass(difficulty: Difficulty, guessHex: String, answerColor: HexColor): Boolean {
        return when (difficulty) {
            Difficulty.EASY -> {
                val feedback = evaluateEasy(guessHex, answerColor)
                feedback.none { it.color == FeedbackColor.RED }
            }
            Difficulty.MEDIUM, Difficulty.HARD -> {
                val guessRgb = HexColorUtils.hexToRgb(guessHex)
                val guessColor = HexColor(guessRgb.first, guessRgb.second, guessRgb.third)
                calculateSimilarity(guessColor, answerColor) >= GameBalanceConfig.PASS_THRESHOLD_MEDIUM_HARD
            }
        }
    }

    fun isPerfectMatch(guessHex: String, answerColor: HexColor): Boolean {
        return guessHex.removePrefix("#").uppercase() == answerColor.hex.uppercase()
    }
}
