package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.model.GameMode
import java.util.Locale

data class PlayerStat(
    val name: String,
    val wins: Int,
    val bestScorePercent: Int
)

internal object PlayerStatKeyCodec {
    fun key(player: String): String {
        val canonical = player.trim().lowercase(Locale.ROOT)
        val readable = canonical.replace(Regex("[^a-z0-9à-ÿ]+"), "_").take(32)
        val hash = canonical.hashCode().toUInt().toString(16).padStart(8, '0')
        return "${readable}_$hash"
    }

    fun legacyKey(player: String): String =
        player.trim().lowercase(Locale.ROOT).replace(Regex("[^a-z0-9à-ÿ]+"), "_").take(48)
}

internal object PlayerStatSanitizer {
    fun wins(value: Int): Int = value.coerceAtLeast(0)

    fun bestScore(value: Int): Int = value.coerceIn(0, 100)

    fun increment(value: Int): Int {
        val safe = wins(value)
        return if (safe == Int.MAX_VALUE) Int.MAX_VALUE else safe + 1
    }
}

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

        val safePercent = PlayerStatSanitizer.bestScore(scorePercent)
        val winsKey = winsKey(winner)
        val bestKey = bestKey(winner)
        val wins = PlayerStatSanitizer.increment(readStat(winsKey, legacyWinsKey(winner)))
        val best = maxOf(
            PlayerStatSanitizer.bestScore(readStat(bestKey, legacyBestKey(winner))),
            safePercent
        )
        val modeKey = "mode_" + mode.name + "_plays"
        val modePlays = PlayerStatSanitizer.increment(preferences.getInt(modeKey, 0))

        preferences.edit()
            .putInt(winsKey, wins)
            .putInt(bestKey, best)
            .putInt(modeKey, modePlays)
            .putBoolean(consumedKey, true)
            .apply()

        return PlayerStat(winner, wins, best)
    }

    fun statFor(player: String): PlayerStat =
        PlayerStat(
            name = player,
            wins = PlayerStatSanitizer.wins(readStat(winsKey(player), legacyWinsKey(player))),
            bestScorePercent = PlayerStatSanitizer.bestScore(
                readStat(bestKey(player), legacyBestKey(player))
            )
        )

    private fun readStat(currentKey: String, legacyKey: String): Int =
        if (preferences.contains(currentKey)) {
            preferences.getInt(currentKey, 0)
        } else {
            preferences.getInt(legacyKey, 0)
        }

    private fun winsKey(player: String) = "wins_v2_" + PlayerStatKeyCodec.key(player)
    private fun bestKey(player: String) = "best_v2_" + PlayerStatKeyCodec.key(player)
    private fun legacyWinsKey(player: String) = "wins_" + PlayerStatKeyCodec.legacyKey(player)
    private fun legacyBestKey(player: String) = "best_" + PlayerStatKeyCodec.legacyKey(player)

    companion object {
        private const val PREFS_NAME = "vibecheck_local_stats"
    }
}
