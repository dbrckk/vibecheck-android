package com.vibecheck.app.data

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionHistoryCodecTest {
    @Test
    fun round_trip_preserves_recent_unique_ids() {
        val encoded = QuestionHistoryCodec.encode(
            listOf(" q1 ", "q2", "q1", "q3"),
            maxIds = 16
        )

        assertEquals(listOf("q1", "q2", "q3"), QuestionHistoryCodec.decode(encoded, 16))
    }

    @Test
    fun only_the_most_recent_ids_are_kept() {
        val ids = (1..24).map { "q$it" }

        assertEquals(
            (9..24).map { "q$it" },
            QuestionHistoryCodec.decode(QuestionHistoryCodec.encode(ids, 16), 16)
        )
    }

    @Test
    fun decode_bounds_legacy_or_oversized_persisted_history() {
        val oversized = (1..40).joinToString("\u001F") { "q$it" }

        assertEquals((25..40).map { "q$it" }, QuestionHistoryCodec.decode(oversized, 16))
    }

    @Test
    fun blank_and_duplicate_entries_are_removed() {
        val raw = listOf("", " q1 ", "q1", "   ", "q2").joinToString("\u001F")

        assertEquals(listOf("q1", "q2"), QuestionHistoryCodec.decode(raw, 16))
    }

    @Test
    fun non_positive_limit_returns_empty_history() {
        assertEquals(emptyList<String>(), QuestionHistoryCodec.decode("q1\u001Fq2", 0))
        assertEquals("", QuestionHistoryCodec.encode(listOf("q1", "q2"), -1))
    }
}
