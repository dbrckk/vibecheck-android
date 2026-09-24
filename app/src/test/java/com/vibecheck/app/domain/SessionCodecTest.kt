package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.Vote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionCodecTest {
    @Test
    fun vote_round_trip_preserves_values() {
        val vote = Vote("who_01", "Alice Martin")
        assertEquals(vote, SessionCodec.decodeVote(SessionCodec.encodeVote(vote)))
    }

    @Test
    fun v2_round_trip_preserves_separator_like_control_character() {
        val separator = 31.toChar().toString()
        val vote = Vote("question" + separator + "id", "Alice" + separator + "Martin")

        assertEquals(vote, SessionCodec.decodeVote(SessionCodec.encodeVote(vote)))
    }

    @Test
    fun legacy_vote_remains_readable() {
        val separator = 31.toChar().toString()
        assertEquals(
            Vote("who_01", "Alice Martin"),
            SessionCodec.decodeVote("who_01" + separator + "Alice Martin")
        )
    }

    @Test
    fun new_votes_use_versioned_format() {
        assertTrue(SessionCodec.encodeVote(Vote("q1", "Alice")).startsWith("v2:"))
    }

    @Test
    fun malformed_vote_is_ignored() {
        assertNull(SessionCodec.decodeVote("invalid"))
        assertNull(SessionCodec.decodeVote("v2:not-a-length:q1Alice"))
        assertNull(SessionCodec.decodeVote("v2:99:q1Alice"))
        assertNull(SessionCodec.decodeVote("v2:2147483647:q1Alice"))
        assertNull(SessionCodec.decodeVote("v2:2:q1"))
    }

    @Test
    fun multiple_votes_round_trip() {
        val votes = listOf(
            Vote("q1", "Alice"),
            Vote("q2", "Bob")
        )
        assertEquals(votes, SessionCodec.decodeVotes(SessionCodec.encodeVotes(votes)))
    }

    @Test
    fun decode_votes_skips_corrupt_entries_without_losing_valid_votes() {
        val valid = Vote("q1", "Alice")
        val values = listOf(
            SessionCodec.encodeVote(valid),
            "v2:99:broken",
            "invalid"
        )

        assertEquals(listOf(valid), SessionCodec.decodeVotes(values))
    }
}
