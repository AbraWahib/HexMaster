package com.abra.hexmaster.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abra.hexmaster.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val soundEnabled: StateFlow<Boolean> = settingsDataStore.soundEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val vibrationEnabled: StateFlow<Boolean> = settingsDataStore.vibrationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setSoundEnabled(enabled)
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setVibrationEnabled(enabled)
        }
    }

    fun resetHighScores() {
        viewModelScope.launch {
            settingsDataStore.resetHighScores()
        }
    }

    class Factory(
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(settingsDataStore) as T
        }
    }
}
