package com.vibecheck.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal object AudioPreferencesDefaults {
    const val PREFS_NAME = "vibecheck_audio"
    const val KEY_ENABLED = "music_enabled"
    const val KEY_VOLUME = "music_volume"
    const val ENABLED = true
    const val VOLUME = 0.18f

    fun sanitizeVolume(value: Float): Float = value.coerceIn(0f, 1f)
}

class AudioPreferencesStore(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        AudioPreferencesDefaults.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _musicEnabled = MutableStateFlow(
        preferences.getBoolean(AudioPreferencesDefaults.KEY_ENABLED, AudioPreferencesDefaults.ENABLED)
    )
    val musicEnabled: StateFlow<Boolean> = _musicEnabled.asStateFlow()

    private val _musicVolume = MutableStateFlow(
        AudioPreferencesDefaults.sanitizeVolume(
            preferences.getFloat(AudioPreferencesDefaults.KEY_VOLUME, AudioPreferencesDefaults.VOLUME)
        )
    )
    val musicVolume: StateFlow<Float> = _musicVolume.asStateFlow()

    fun setMusicEnabled(enabled: Boolean) {
        if (_musicEnabled.value == enabled) return
        _musicEnabled.value = enabled
        preferences.edit().putBoolean(AudioPreferencesDefaults.KEY_ENABLED, enabled).apply()
    }

    fun setMusicVolume(volume: Float) {
        val safeVolume = AudioPreferencesDefaults.sanitizeVolume(volume)
        if (_musicVolume.value == safeVolume) return
        _musicVolume.value = safeVolume
        preferences.edit().putFloat(AudioPreferencesDefaults.KEY_VOLUME, safeVolume).apply()
    }
}
