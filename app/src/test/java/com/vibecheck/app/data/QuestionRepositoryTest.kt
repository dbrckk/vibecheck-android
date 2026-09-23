package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class QuestionRepositoryTest {
    @Test
    fun same_seed_returns_same_session() {
        val first = QuestionRepository.forMode(GameMode.WHO_OF_US, seed = 42L)
        val second = QuestionRepository.forMode(GameMode.WHO_OF_US, seed = 42L)
        assertEquals(first.map { it.id }, second.map { it.id })
    }

    @Test
    fun session_is_limited_to_eight_by_default() {
        assertEquals(8, QuestionRepository.forMode(GameMode.RED_GREEN, seed = 1L).size)
    }

    @Test
    fun different_seeds_can_change_question_order() {
        val first = QuestionRepository.forMode(GameMode.MOST_LIKELY, seed = 1L)
        val second = QuestionRepository.forMode(GameMode.MOST_LIKELY, seed = 2L)
        assertNotEquals(first.map { it.id }, second.map { it.id })
    }
}
