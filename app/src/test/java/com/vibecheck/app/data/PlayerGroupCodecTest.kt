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
    fun v2_wire_format_stays_compatible_with_existing_saves() {
        assertEquals(
            "v2:QWxpY2U.w4lsb2RpZQ.QR9C.RG90Lk5hbWU",
            PlayerGroupCodec.encode(listOf("Alice", "Élodie", "A\u001FB", "Dot.Name"))
        )
        assertEquals(
            listOf("Alice", "Élodie", "A\u001FB", "Dot.Name"),
            PlayerGroupCodec.decode("v2:QWxpY2U.w4lsb2RpZQ.QR9C.RG90Lk5hbWU")
        )
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
    fun malformed_utf8_item_is_dropped_without_inventing_a_player() {
        assertEquals(
            listOf("Alice", "Bob"),
            PlayerGroupCodec.decode("v2:QWxpY2U.wA.Qm9i")
        )
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
