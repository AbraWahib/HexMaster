package com.abra.hexmaster.core

import android.content.Context
import android.media.MediaPlayer
import com.abra.hexmaster.data.preferences.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SoundManager(
    private val context: Context,
    private val settingsDataStore: SettingsDataStore
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun playSound(soundName: String) {
        scope.launch {
            if (settingsDataStore.soundEnabled.first()) {
                val resId = context.resources.getIdentifier(soundName, "raw", context.packageName)
                if (resId != 0) {
                    val mediaPlayer = MediaPlayer.create(context, resId)
                    mediaPlayer?.setOnCompletionListener { it.release() }
                    mediaPlayer?.start()
                }
            }
        }
    }
}
