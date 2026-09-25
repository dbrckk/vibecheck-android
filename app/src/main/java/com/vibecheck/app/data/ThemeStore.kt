package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.ui.theme.VibeThemeStyle

class ThemeStore(context: Context) {
    private val preferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun load(): VibeThemeStyle =
        VibeThemeStyle.fromStoredId(preferences.getString(KEY_THEME, null))

    fun save(style: VibeThemeStyle) {
        preferences.edit().putString(KEY_THEME, style.id).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "vibecheck_theme"
        const val KEY_THEME = "selected_theme"
    }
}
