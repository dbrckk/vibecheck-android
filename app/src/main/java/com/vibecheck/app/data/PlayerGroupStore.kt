package com.vibecheck.app.data

import android.content.Context
import com.vibecheck.app.domain.PlayerRules
import java.nio.charset.StandardCharsets

private object Base64UrlCodec {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"

    fun encode(bytes: ByteArray): String {
        if (bytes.isEmpty()) return ""
        val result = StringBuilder((bytes.size * 4 + 2) / 3)
        var index = 0
        while (index < bytes.size) {
            val first = bytes[index++].toInt() and 0xff
            val hasSecond = index < bytes.size
            val second = if (hasSecond) bytes[index++].toInt() and 0xff else 0
            val hasThird = index < bytes.size
            val third = if (hasThird) bytes[index++].toInt() and 0xff else 0

            result.append(ALPHABET[first ushr 2])
            result.append(ALPHABET[((first and 0x03) shl 4) or (second ushr 4)])
            if (hasSecond) {
                result.append(ALPHABET[((second and 0x0f) shl 2) or (third ushr 6)])
            }
            if (hasThird) {
                result.append(ALPHABET[third and 0x3f])
            }
        }
        return result.toString()
    }

    fun decode(value: String): ByteArray? {
        if (value.isEmpty()) return byteArrayOf()
        if (value.length % 4 == 1) return null

        val output = ArrayList<Byte>((value.length * 3) / 4)
        var buffer = 0
        var bits = 0
        for (character in value) {
            val decoded = ALPHABET.indexOf(character)
            if (decoded < 0) return null
            buffer = (buffer shl 6) or decoded
            bits += 6
            if (bits >= 8) {
                bits -= 8
                output.add(((buffer ushr bits) and 0xff).toByte())
            }
        }
        return output.toByteArray()
    }
}

object PlayerGroupCodec {
    private const val LEGACY_SEPARATOR = "\u001F"
    private const val V2_PREFIX = "v2:"
    private const val ITEM_SEPARATOR = "."

    fun encode(players: List<String>): String =
        V2_PREFIX + sanitize(players).joinToString(ITEM_SEPARATOR) { player ->
            Base64UrlCodec.encode(player.toByteArray(StandardCharsets.UTF_8))
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
        return payload.split(ITEM_SEPARATOR).mapNotNull { encoded ->
            Base64UrlCodec.decode(encoded)?.let { bytes ->
                String(bytes, StandardCharsets.UTF_8)
            }
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
