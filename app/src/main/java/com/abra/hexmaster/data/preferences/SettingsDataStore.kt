package com.abra.hexmaster.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.abra.hexmaster.data.model.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val HIGH_SCORE_OVERALL = intPreferencesKey("high_score_overall")
        val HIGH_SCORE_EASY = intPreferencesKey("high_score_easy")
        val HIGH_SCORE_MEDIUM = intPreferencesKey("high_score_medium")
        val HIGH_SCORE_HARD = intPreferencesKey("high_score_hard")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }

    val highScores: Flow<Map<String, Int>> = context.dataStore.data.map { prefs ->
        mapOf(
            "overall" to (prefs[HIGH_SCORE_OVERALL] ?: 0),
            Difficulty.EASY.name to (prefs[HIGH_SCORE_EASY] ?: 0),
            Difficulty.MEDIUM.name to (prefs[HIGH_SCORE_MEDIUM] ?: 0),
            Difficulty.HARD.name to (prefs[HIGH_SCORE_HARD] ?: 0)
        )
    }

    val bestStreak: Flow<Int> = context.dataStore.data.map { it[BEST_STREAK] ?: 0 }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[SOUND_ENABLED] ?: true }
    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[VIBRATION_ENABLED] ?: true }

    suspend fun saveHighScore(score: Int, difficulty: Difficulty) {
        context.dataStore.edit { prefs ->
            val currentOverall = prefs[HIGH_SCORE_OVERALL] ?: 0
            if (score > currentOverall) {
                prefs[HIGH_SCORE_OVERALL] = score
            }

            val diffKey = when (difficulty) {
                Difficulty.EASY -> HIGH_SCORE_EASY
                Difficulty.MEDIUM -> HIGH_SCORE_MEDIUM
                Difficulty.HARD -> HIGH_SCORE_HARD
            }
            val currentDiff = prefs[diffKey] ?: 0
            if (score > currentDiff) {
                prefs[diffKey] = score
            }
        }
    }

    suspend fun saveBestStreak(streak: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[BEST_STREAK] ?: 0
            if (streak > current) {
                prefs[BEST_STREAK] = streak
            }
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }
    
    suspend fun resetHighScores() {
        context.dataStore.edit { prefs ->
            prefs.remove(HIGH_SCORE_OVERALL)
            prefs.remove(HIGH_SCORE_EASY)
            prefs.remove(HIGH_SCORE_MEDIUM)
            prefs.remove(HIGH_SCORE_HARD)
            prefs.remove(BEST_STREAK)
        }
    }
}
