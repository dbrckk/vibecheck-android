package com.vibecheck.app.domain.solo

import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloRoundResolverTest {
    private val resolver = SoloRoundResolver()
    private val cast = PersonaCatalog.all.take(5)
    private val question = Question("who_test", GameMode.WHO_OF_US, "Qui improviserait le mieux ?")

    @Test
    fun every_persona_casts_one_valid_vote() {
        val round = resolver.resolve(question, cast, 42L)
        assertEquals(cast.size, round.votes.size)
        val ids = cast.map { it.id }.toSet()
        round.votes.forEach { vote ->
            assertTrue(vote.voterId in ids)
            assertTrue(vote.targetPersonaId in ids)
        }
        assertTrue(round.winnerPersonaId in ids)
    }

    @Test
    fun same_question_cast_and_seed_are_deterministic() {
        val first = resolver.resolve(question, cast, 987L)
        val second = resolver.resolve(question, cast, 987L)
        assertEquals(first, second)
    }

    @Test
    fun empty_cast_is_safe() {
        val round = resolver.resolve(question, emptyList(), 1L)
        assertTrue(round.votes.isEmpty())
        assertEquals(null, round.winnerPersonaId)
    }
}
