package com.vibecheck.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vibecheck.app.ui.theme.VibeThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.appearanceDataStore by preferencesDataStore(name = "appearance")

class AppearanceStore(context: Context) {
    private val dataStore: DataStore<Preferences> =
        context.applicationContext.appearanceDataStore

    val selectedStyle: Flow<VibeThemeStyle> = dataStore.data
        .catch {
            emit(androidx.datastore.preferences.core.emptyPreferences())
        }
        .map { preferences ->
            VibeThemeStyle.fromStoredId(preferences[KEY_THEME])
        }

    suspend fun save(style: VibeThemeStyle) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = style.id
        }
    }

    private companion object {
        val KEY_THEME = stringPreferencesKey("selected_theme")
    }
}
