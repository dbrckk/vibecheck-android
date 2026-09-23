package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.Vote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SessionCodecTest {
    @Test
    fun vote_round_trip_preserves_values() {
        val vote = Vote("who_01", "Alice Martin")
        assertEquals(vote, SessionCodec.decodeVote(SessionCodec.encodeVote(vote)))
    }

    @Test
    fun malformed_vote_is_ignored() {
        assertNull(SessionCodec.decodeVote("invalid"))
    }

    @Test
    fun multiple_votes_round_trip() {
        val votes = listOf(
            Vote("q1", "Alice"),
            Vote("q2", "Bob")
        )
        assertEquals(votes, SessionCodec.decodeVotes(SessionCodec.encodeVotes(votes)))
    }
}
