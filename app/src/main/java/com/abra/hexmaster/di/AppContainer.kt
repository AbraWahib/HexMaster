package com.abra.hexmaster.di

import android.content.Context
import com.abra.hexmaster.core.color.ColorGenerator
import com.abra.hexmaster.data.preferences.SettingsDataStore
import com.abra.hexmaster.domain.GameEngine
import com.abra.hexmaster.data.model.Difficulty

import com.abra.hexmaster.core.SoundManager

/**
 * Dependency injection container for the application.
 */
class AppContainer(private val context: Context) {

    val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStore(context)
    }

    val soundManager: SoundManager by lazy {
        SoundManager(context, settingsDataStore)
    }

    val colorGenerator: ColorGenerator by lazy {
        ColorGenerator()
    }

    /**
     * Creates a new GameEngine instance for a new game session.
     */
    fun createGameEngine(difficulty: Difficulty): GameEngine {
        return GameEngine(colorGenerator, difficulty)
    }
}
