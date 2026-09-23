package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.PlayerRules

object PlayerGroupCodec {
    private const val SEPARATOR = "\u001F"

    fun encode(players: List<String>): String =
        sanitize(players).joinToString(SEPARATOR)

    fun decode(raw: String?): List<String> =
        sanitize(raw.orEmpty().split(SEPARATOR))

    private fun sanitize(players: List<String>): List<String> =
        players.fold(emptyList()) { accepted, candidate ->
            if (PlayerRules.canAdd(accepted, candidate)) {
                accepted + PlayerRules.normalize(candidate)
            } else {
                accepted
            }
        }
}

class PlayerGroupStore(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<String> =
        PlayerGroupCodec.decode(preferences.getString(KEY_PLAYERS, null))

    fun save(players: List<String>) {
        preferences.edit()
            .putString(KEY_PLAYERS, PlayerGroupCodec.encode(players))
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "vibecheck_group"
        private const val KEY_PLAYERS = "players"
    }
}
