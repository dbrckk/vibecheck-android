package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.model.GameMode

class QuestionHistoryStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun recentIds(mode: GameMode): Set<String> =
        recentList(mode).toSet()

    fun remember(mode: GameMode, ids: List<String>) {
        if (ids.isEmpty()) return
        val updated = (recentList(mode) + ids)
            .distinct()
            .takeLast(MAX_RECENT_IDS)
        preferences.edit()
            .putString(key(mode), updated.joinToString(SEPARATOR))
            .apply()
    }

    fun clear(mode: GameMode) {
        preferences.edit().remove(key(mode)).apply()
    }

    private fun recentList(mode: GameMode): List<String> =
        preferences.getString(key(mode), null)
            ?.split(SEPARATOR)
            ?.filter { it.isNotBlank() }
            .orEmpty()

    private fun key(mode: GameMode) = "recent_" + mode.name

    companion object {
        private const val PREFS_NAME = "vibecheck_question_history"
        private const val MAX_RECENT_IDS = 16
        private const val SEPARATOR = "\u001F"
    }
}
