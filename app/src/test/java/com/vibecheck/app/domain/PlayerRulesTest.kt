package com.vibecheck.app.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerRulesTest {
    @Test
    fun needs_at_least_two_players() {
        assertFalse(PlayerRules.canStart(listOf("Alex")))
        assertTrue(PlayerRules.canStart(listOf("Alex", "Sam")))
    }

    @Test
    fun rejects_duplicates_case_insensitively() {
        assertFalse(PlayerRules.canAdd(listOf("Alex"), " alex "))
    }

    @Test
    fun rejects_more_than_eight_players() {
        val players = (1..8).map { "Joueur $it" }
        assertFalse(PlayerRules.canAdd(players, "Joueur 9"))
    }

    @Test
    fun rejects_too_long_names() {
        assertFalse(PlayerRules.canAdd(emptyList(), "ABCDEFGHIJKLMNOPQRS"))
    }
}
