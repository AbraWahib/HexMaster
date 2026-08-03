package com.abra.hexmaster.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abra.hexmaster.core.GameBalanceConfig
import com.abra.hexmaster.core.SoundManager
import com.abra.hexmaster.core.color.HexColorUtils
import com.abra.hexmaster.data.model.Difficulty
import com.abra.hexmaster.data.model.GameUiState
import com.abra.hexmaster.data.preferences.SettingsDataStore
import com.abra.hexmaster.domain.GameEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class GameViewModel(
    private val gameEngine: GameEngine,
    private val soundManager: SoundManager,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val uiState: StateFlow<GameUiState> = gameEngine.state

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isInputError = MutableStateFlow(false)
    val isInputError: StateFlow<Boolean> = _isInputError.asStateFlow()

    private var timerJob: Job? = null

    init {
        if (uiState.value.difficulty == Difficulty.HARD) {
            startTimer()
        }
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnInputChanged -> onInputChanged(event.text)
            is GameEvent.SubmitGuess -> submitGuess()
            is GameEvent.NextRound -> nextRound()
            is GameEvent.OnShakeComplete -> onShakeComplete()
            is GameEvent.OnQuitConfirmed -> quitGame()
        }
    }

    private fun onInputChanged(text: String) {
        if (text.length <= 6) {
            _inputText.value = text
            _isInputError.value = false
        }
    }

    private fun submitGuess() {
        val guess = _inputText.value
        if (guess.length < 6 || !HexColorUtils.isValidHex(guess)) {
            _isInputError.value = true
            return
        }

        soundManager.playSound("sfx_submit")
        gameEngine.submitGuess(guess, uiState.value.remainingTimeSeconds)
        
        val result = uiState.value.lastRoundResult
        if (result != null) {
            if (result.isPass) {
                soundManager.playSound("sfx_correct")
            } else {
                soundManager.playSound("sfx_wrong")
                // Reset input for the next try if round is not over
                if (uiState.value.triesUsed < GameBalanceConfig.MAX_TRIES_PER_ROUND) {
                    _inputText.value = ""
                }
            }
        }
        
        if (uiState.value.isGameOver) {
            saveResult()
        }
        
        timerJob?.cancel()
    }

    private fun nextRound() {
        gameEngine.nextRound()
        _inputText.value = ""
        if (uiState.value.difficulty == Difficulty.HARD) {
            startTimer()
        }
    }

    private fun onShakeComplete() {
        _isInputError.value = false
    }

    private fun quitGame() {
        saveResult()
        gameEngine.endGame()
    }

    private fun saveResult() {
        viewModelScope.launch {
            settingsDataStore.saveHighScore(uiState.value.score, uiState.value.difficulty)
            settingsDataStore.saveBestStreak(uiState.value.bestStreakInSession)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (uiState.value.remainingTimeSeconds > 0 && !uiState.value.isGameOver && uiState.value.lastRoundResult == null) {
                delay(1000.milliseconds)
                gameEngine.onTick()
                if (uiState.value.remainingTimeSeconds > 0) {
                    soundManager.playSound("sfx_tick")
                }
            }
            if (uiState.value.remainingTimeSeconds == 0L && uiState.value.lastRoundResult == null) {
                submitGuess()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    class Factory(
        private val gameEngine: GameEngine,
        private val soundManager: SoundManager,
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(gameEngine, soundManager, settingsDataStore) as T
        }
    }
}
