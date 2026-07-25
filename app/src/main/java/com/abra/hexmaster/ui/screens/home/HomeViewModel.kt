package com.abra.hexmaster.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abra.hexmaster.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    settingsDataStore: SettingsDataStore
) : ViewModel() {

    val highScores: StateFlow<Map<String, Int>> = settingsDataStore.highScores
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    val bestStreak: StateFlow<Int> = settingsDataStore.bestStreak
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    class Factory(
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(settingsDataStore) as T
        }
    }
}
