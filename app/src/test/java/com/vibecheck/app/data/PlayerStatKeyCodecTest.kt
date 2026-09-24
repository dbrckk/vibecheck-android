package com.vibecheck.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerStatKeyCodecTest {
    @Test
    fun key_is_stable_for_equivalent_case_and_whitespace() {
        assertEquals(
            PlayerStatKeyCodec.key("  Élodie  "),
            PlayerStatKeyCodec.key("élodie")
        )
    }

    @Test
    fun key_separates_names_that_collided_in_legacy_storage() {
        assertEquals(
            PlayerStatKeyCodec.legacyKey("Jean Paul"),
            PlayerStatKeyCodec.legacyKey("Jean-Paul")
        )
        assertNotEquals(
            PlayerStatKeyCodec.key("Jean Paul"),
            PlayerStatKeyCodec.key("Jean-Paul")
        )
    }

    @Test
    fun key_separates_long_names_with_same_legacy_prefix() {
        val prefix = "a".repeat(48)
        val first = prefix + "x"
        val second = prefix + "y"

        assertEquals(PlayerStatKeyCodec.legacyKey(first), PlayerStatKeyCodec.legacyKey(second))
        assertNotEquals(PlayerStatKeyCodec.key(first), PlayerStatKeyCodec.key(second))
    }

    @Test
    fun key_is_safe_and_bounded() {
        val key = PlayerStatKeyCodec.key(" Zoë / 👾 / Test très très long " + "x".repeat(100))

        assertTrue(key.matches(Regex("[a-z0-9à-ÿ_]+")))
        assertTrue(key.length <= 41)
    }

    @Test
    fun persisted_stats_are_clamped_to_valid_ranges() {
        assertEquals(0, PlayerStatSanitizer.wins(-5))
        assertEquals(7, PlayerStatSanitizer.wins(7))
        assertEquals(0, PlayerStatSanitizer.bestScore(-1))
        assertEquals(100, PlayerStatSanitizer.bestScore(250))
    }

    @Test
    fun increment_saturates_instead_of_overflowing() {
        assertEquals(Int.MAX_VALUE, PlayerStatSanitizer.increment(Int.MAX_VALUE))
        assertEquals(1, PlayerStatSanitizer.increment(-50))
    }
}
