package com.vibecheck.app.data

import android.content.Context

class OnboardingStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isCompleted(): Boolean =
        preferences.getBoolean(KEY_COMPLETED, false)

    fun markCompleted() {
        preferences.edit().putBoolean(KEY_COMPLETED, true).apply()
    }

    companion object {
        private const val PREFS_NAME = "vibecheck_onboarding"
        private const val KEY_COMPLETED = "completed"
    }
}
