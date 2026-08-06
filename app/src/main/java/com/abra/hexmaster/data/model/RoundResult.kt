package com.abra.hexmaster.data.model

import com.abra.hexmaster.core.color.HexColor

data class RoundResult(
    val guessHex: String,
    val answerColor: HexColor,
    val isPass: Boolean,
    val similarity: Double,
    val score: Int,
    val feedback: List<DigitFeedback> = emptyList()
)
