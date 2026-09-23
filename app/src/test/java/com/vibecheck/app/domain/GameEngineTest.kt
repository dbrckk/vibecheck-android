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
    @Test
    fun ranking_orders_by_votes_then_name() {
        val votes = listOf(
            Vote("q1", "Sam"),
            Vote("q2", "Alex"),
            Vote("q3", "Sam"),
            Vote("q4", "Chloe"),
            Vote("q5", "Alex"),
            Vote("q6", "Bob")
        )

        assertEquals(
            listOf(
                "Alex" to 2,
                "Sam" to 2,
                "Bob" to 1,
                "Chloe" to 1
            ),
            GameEngine.ranking(votes)
        )
    }

    @Test
    fun ranking_is_empty_when_there_are_no_votes() {
        assertEquals(emptyList<Pair<String, Int>>(), GameEngine.ranking(emptyList()))
    }
}
