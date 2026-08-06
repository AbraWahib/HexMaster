package com.abra.hexmaster.data.model

enum class FeedbackColor {
    GREEN, YELLOW, RED
}

enum class ArrowIndicator {
    NONE, UP, DOWN, UP_UP, DOWN_DOWN
}

data class DigitFeedback(
    val color: FeedbackColor,
    val arrow: ArrowIndicator
)
