package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity

import org.junit.Assert.assertEquals
import org.junit.Test

class KnowMeRepositoryTest {
    @Test
    fun same_seed_returns_same_hot_seat_prompts() {
        val first = KnowMeRepository.forSeed(seed = 42L)
        val second = KnowMeRepository.forSeed(seed = 42L)

        assertEquals(first.map { it.id }, second.map { it.id })
    }

    @Test
    fun hot_seat_catalog_has_twenty_four_unique_prompts() {
        val prompts = KnowMeRepository.forSeed(seed = 1L, limit = 24)

        assertEquals(24, prompts.size)
        assertEquals(24, prompts.map { it.id }.distinct().size)
    }

    @Test
    fun recent_hot_seat_prompts_are_deprioritized() {
        val baseline = KnowMeRepository.forSeed(seed = 123L, limit = 8)
        val next = KnowMeRepository.forSeed(
            seed = 123L,
            limit = 8,
            avoidIds = baseline.map { it.id }.toSet()
        )

        assertEquals(0, next.count { prompt -> baseline.any { it.id == prompt.id } })
    }
    @Test
    fun every_hot_seat_intensity_has_sixteen_prompts() {
        GameIntensity.entries.forEach { intensity ->
            val prompts = KnowMeRepository.forSeed(
                seed = 9L,
                limit = 24,
                intensity = intensity
            )
            assertEquals(16, prompts.size)
        }
    }
}
