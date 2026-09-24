package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.model.GameMode

object QuestionHistoryCodec {
    private const val SEPARATOR = "\u001F"

    fun encode(ids: List<String>, maxIds: Int): String =
        sanitize(ids, maxIds).joinToString(SEPARATOR)

    fun decode(raw: String?, maxIds: Int): List<String> =
        sanitize(raw?.split(SEPARATOR).orEmpty(), maxIds)

    private fun sanitize(ids: List<String>, maxIds: Int): List<String> {
        if (maxIds <= 0) return emptyList()
        return ids.asSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .distinct()
            .toList()
            .takeLast(maxIds)
    }
}

class QuestionHistoryStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun recentIds(mode: GameMode): Set<String> =
        recentList(mode).toSet()

    fun remember(mode: GameMode, ids: List<String>) {
        if (ids.isEmpty()) return
        val updated = QuestionHistoryCodec.encode(
            recentList(mode) + ids,
            MAX_RECENT_IDS
        )
        preferences.edit()
            .putString(key(mode), updated)
            .apply()
    }

    fun clear(mode: GameMode) {
        preferences.edit().remove(key(mode)).apply()
    }

    private fun recentList(mode: GameMode): List<String> =
        QuestionHistoryCodec.decode(
            preferences.getString(key(mode), null),
            MAX_RECENT_IDS
        )

    private fun key(mode: GameMode) = "recent_" + mode.name

    companion object {
        private const val PREFS_NAME = "vibecheck_question_history"
        private const val MAX_RECENT_IDS = 16
    }
}
