package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.PlayerRules
import java.nio.charset.StandardCharsets
import java.util.Base64

object PlayerGroupCodec {
    private const val LEGACY_SEPARATOR = "\u001F"
    private const val V2_PREFIX = "v2:"
    private const val ITEM_SEPARATOR = "."

    fun encode(players: List<String>): String {
        val encoder = Base64.getUrlEncoder().withoutPadding()
        return V2_PREFIX + sanitize(players).joinToString(ITEM_SEPARATOR) { player ->
            encoder.encodeToString(player.toByteArray(StandardCharsets.UTF_8))
        }
    }

    fun decode(raw: String?): List<String> {
        val value = raw.orEmpty()
        if (value.isBlank()) return emptyList()

        val decoded = if (value.startsWith(V2_PREFIX)) {
            decodeV2(value.removePrefix(V2_PREFIX))
        } else {
            value.split(LEGACY_SEPARATOR)
        }
        return sanitize(decoded)
    }

    private fun decodeV2(payload: String): List<String> {
        if (payload.isBlank()) return emptyList()
        val decoder = Base64.getUrlDecoder()
        return payload.split(ITEM_SEPARATOR).mapNotNull { encoded ->
            runCatching {
                String(decoder.decode(encoded), StandardCharsets.UTF_8)
            }.getOrNull()
        }
    }

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
