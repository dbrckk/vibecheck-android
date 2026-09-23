package com.vibecheck.app.data

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerGroupCodecTest {
    @Test
    fun round_trip_normalizes_and_preserves_valid_players() {
        val encoded = PlayerGroupCodec.encode(listOf(" Alice ", "Bob", "Chloe"))
        val decoded = PlayerGroupCodec.decode(encoded)

        assertEquals(listOf("Alice", "Bob", "Chloe"), decoded)
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
