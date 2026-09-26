package com.vibecheck.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vibecheck.app.ui.theme.VibeThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appearanceDataStore by preferencesDataStore(name = "appearance")

class AppearanceStore(private val context: Context) {
    val theme: Flow<VibeThemeStyle> = context.appearanceDataStore.data.map { preferences ->
        VibeThemeStyle.fromId(preferences[THEME_KEY])
    }

    suspend fun setTheme(style: VibeThemeStyle) {
        context.appearanceDataStore.edit { preferences ->
            preferences[THEME_KEY] = style.id
        }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_style")
    }
}
