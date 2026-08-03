package com.abra.hexmaster.ui.screens.game

sealed class GameEvent {
    data class OnInputChanged(val text: String) : GameEvent()
    object SubmitGuess : GameEvent()
    object NextRound : GameEvent()
    object OnShakeComplete : GameEvent()
    object OnQuitConfirmed : GameEvent()
}