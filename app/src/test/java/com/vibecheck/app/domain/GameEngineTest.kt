package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.Vote
import org.junit.Assert.assertEquals
import org.junit.Test

class GameEngineTest {
    @Test
    fun winner_is_highest_vote_count() {
        val votes = listOf(
            Vote("q1", "Alex"),
            Vote("q2", "Sam"),
            Vote("q3", "Alex")
        )
        val result = GameEngine.result(votes)
        assertEquals("Alex", result.winner)
        assertEquals(2, result.score)
        assertEquals(3, result.total)
    }

    @Test
    fun tie_is_deterministic() {
        val votes = listOf(Vote("q1", "Sam"), Vote("q2", "Alex"))
        assertEquals("Alex", GameEngine.result(votes).winner)
    }

    @Test
    fun empty_votes_return_safe_result() {
        val result = GameEngine.result(emptyList())
        assertEquals("Personne", result.winner)
        assertEquals(0, result.score)
        assertEquals(0, result.total)
    }
}
