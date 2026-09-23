package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.model.GameMode

data class PlayerStat(
    val name: String,
    val wins: Int,
    val bestScorePercent: Int
)

class LocalStatsStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun recordResult(
        sessionKey: String,
        mode: GameMode,
        winner: String,
        scorePercent: Int
    ): PlayerStat? {
        if (sessionKey.isBlank() || winner.isBlank()) return null
        val consumedKey = "session_" + sessionKey
        if (preferences.getBoolean(consumedKey, false)) return statFor(winner)

        val safePercent = scorePercent.coerceIn(0, 100)
        val winsKey = winsKey(winner)
        val bestKey = bestKey(winner)
        val wins = preferences.getInt(winsKey, 0) + 1
        val best = maxOf(preferences.getInt(bestKey, 0), safePercent)
        val modeKey = "mode_" + mode.name + "_plays"

        preferences.edit()
            .putInt(winsKey, wins)
            .putInt(bestKey, best)
            .putInt(modeKey, preferences.getInt(modeKey, 0) + 1)
            .putBoolean(consumedKey, true)
            .apply()

        return PlayerStat(winner, wins, best)
    }

    fun statFor(player: String): PlayerStat =
        PlayerStat(
            name = player,
            wins = preferences.getInt(winsKey(player), 0),
            bestScorePercent = preferences.getInt(bestKey(player), 0)
        )

    private fun winsKey(player: String) = "wins_" + normalizeKey(player)
    private fun bestKey(player: String) = "best_" + normalizeKey(player)

    private fun normalizeKey(player: String): String =
        player.trim().lowercase().replace(Regex("[^a-z0-9à-ÿ]+"), "_").take(48)

    companion object {
        private const val PREFS_NAME = "vibecheck_local_stats"
    }
}