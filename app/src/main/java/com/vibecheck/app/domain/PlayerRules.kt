package com.vibecheck.app.domain

import java.util.Locale

object PlayerRules {
    const val MIN_PLAYERS = 2
    const val MAX_PLAYERS = 8
    const val MAX_NAME_LENGTH = 18

    fun normalize(name: String): String = name.trim().replace(Regex("\\s+"), " ")

    fun canAdd(players: List<String>, candidate: String): Boolean {
        val normalized = normalize(candidate)
        if (players.size >= MAX_PLAYERS) return false
        if (normalized.isBlank() || normalized.length > MAX_NAME_LENGTH) return false
        return players.none {
            normalize(it).lowercase(Locale.ROOT) == normalized.lowercase(Locale.ROOT)
        }
    }

    fun canStart(players: List<String>): Boolean {
        val normalized = players.map(::normalize)
        if (normalized.size !in MIN_PLAYERS..MAX_PLAYERS) return false
        if (normalized.any { it.isBlank() || it.length > MAX_NAME_LENGTH }) return false
        return normalized
            .map { it.lowercase(Locale.ROOT) }
            .distinct()
            .size == normalized.size
    }
}
