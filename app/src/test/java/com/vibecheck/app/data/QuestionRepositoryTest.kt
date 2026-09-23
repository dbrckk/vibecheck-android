package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity
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
    @Test
    fun catalog_has_enough_variety_per_mode() {
        GameMode.entries.forEach { mode ->
            assertEquals(24, QuestionRepository.forMode(mode, seed = 123L, limit = 24).size)
        }
    }

    @Test
    fun catalog_question_ids_are_unique_per_mode() {
        GameMode.entries.forEach { mode ->
            val questions = QuestionRepository.forMode(mode, seed = 456L, limit = 24)
            assertEquals(questions.size, questions.map { it.id }.distinct().size)
        }
    }
    @Test
    fun recent_questions_are_deprioritized_when_enough_unseen_exist() {
        val baseline = QuestionRepository.forMode(
            GameMode.WHO_OF_US,
            seed = 99L,
            limit = 8
        )
        val next = QuestionRepository.forMode(
            GameMode.WHO_OF_US,
            seed = 99L,
            limit = 8,
            avoidIds = baseline.map { it.id }.toSet()
        )

        assertEquals(0, next.count { question -> baseline.any { it.id == question.id } })
    }

    @Test
    fun recent_questions_fall_back_when_unseen_pool_is_too_small() {
        val all = QuestionRepository.forMode(
            GameMode.RED_GREEN,
            seed = 7L,
            limit = 24
        )
        val next = QuestionRepository.forMode(
            GameMode.RED_GREEN,
            seed = 7L,
            limit = 8,
            avoidIds = all.dropLast(3).map { it.id }.toSet()
        )

        assertEquals(8, next.size)
        assertEquals(3, next.take(3).count { it.id !in all.dropLast(3).map { q -> q.id }.toSet() })
    }
    @Test
    fun every_intensity_keeps_sixteen_questions_per_mode() {
        GameMode.entries.forEach { mode ->
            GameIntensity.entries.forEach { intensity ->
                val questions = QuestionRepository.forMode(
                    mode = mode,
                    seed = 3L,
                    limit = 24,
                    intensity = intensity
                )
                assertEquals(16, questions.size)
            }
        }
    }
}
