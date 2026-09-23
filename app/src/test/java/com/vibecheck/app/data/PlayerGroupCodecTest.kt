package com.vibecheck.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerGroupCodecTest {
    @Test
    fun round_trip_normalizes_and_preserves_valid_players() {
        val encoded = PlayerGroupCodec.encode(listOf(" Alice ", "Bob", "Chloe"))
        val decoded = PlayerGroupCodec.decode(encoded)

        assertTrue(encoded.startsWith("v2:"))
        assertEquals(listOf("Alice", "Bob", "Chloe"), decoded)
    }

    @Test
    fun separator_characters_inside_names_round_trip_safely() {
        val players = listOf("A\u001FB", "Dot.Name", "Élodie")

        assertEquals(players, PlayerGroupCodec.decode(PlayerGroupCodec.encode(players)))
    }

    @Test
    fun legacy_persisted_groups_are_still_readable() {
        val legacy = listOf("Alice", " Bob ", "Chloe").joinToString("\u001F")

        assertEquals(listOf("Alice", "Bob", "Chloe"), PlayerGroupCodec.decode(legacy))
    }

    @Test
    fun malformed_v2_items_are_ignored_without_losing_valid_players() {
        val encoded = PlayerGroupCodec.encode(listOf("Alice", "Bob"))
        val corrupted = encoded.replaceFirst(".", ".%%%.")

        assertEquals(listOf("Alice", "Bob"), PlayerGroupCodec.decode(corrupted))
    }

    @Test
    fun invalid_and_case_insensitive_duplicates_are_dropped() {
        val tooLong = "x".repeat(30)
        val encoded = PlayerGroupCodec.encode(
            listOf("Alice", " alice ", "", tooLong, "Bob")
        )

        assertEquals(listOf("Alice", "Bob"), PlayerGroupCodec.decode(encoded))
    }

    @Test
    fun persisted_group_never_exceeds_player_limit() {
        val players = (1..12).map { "P$it" }

        assertEquals(8, PlayerGroupCodec.decode(PlayerGroupCodec.encode(players)).size)
    }
}
