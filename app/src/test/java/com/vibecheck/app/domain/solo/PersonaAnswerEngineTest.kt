package com.vibecheck.app.domain.solo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonaAnswerEngineTest {
    private val persona = Persona(
        id = "test_public",
        displayName = "Test Public",
        kind = PersonaKind.PUBLIC_SIMULATION,
        presentation = PersonaPresentation.NEUTRAL,
        archetype = "Audacieux, sociable et joueur",
        traits = PersonaTraits(
            humor = 80,
            boldness = 90,
            empathy = 45,
            sociability = 85,
            competitiveness = 75,
            romanticism = 35,
            chaos = 70,
            deliberation = 30,
        ),
    )

    @Test
    fun `same persona question options and seed always produce same answer`() {
        val first = PersonaAnswerEngine.answer(
            persona = persona,
            questionId = "q42",
            questionText = "Qui tenterait le choix le plus risqué ?",
            options = listOf("Alex", "Sam", "Charlie"),
            seed = 1234L,
        )
        val second = PersonaAnswerEngine.answer(
            persona = persona,
            questionId = "q42",
            questionText = "Qui tenterait le choix le plus risqué ?",
            options = listOf("Alex", "Sam", "Charlie"),
            seed = 1234L,
        )

        assertEquals(first, second)
    }

    @Test
    fun `answer is always one of supplied options and confidence is bounded`() {
        val options = listOf("Green Flag", "Red Flag")
        val answer = PersonaAnswerEngine.answer(
            persona = persona,
            questionId = "red_green_1",
            questionText = "Partir en voyage sur un coup de tête",
            options = options,
            seed = 7L,
        )

        assertTrue(answer.option in options)
        assertTrue(answer.confidence in 0..100)
    }

    @Test
    fun `public persona answer is explicitly marked as fictional simulation`() {
        val answer = PersonaAnswerEngine.answer(
            persona = persona,
            questionId = "q1",
            questionText = "Un choix ?",
            options = listOf("A", "B"),
            seed = 9L,
        )

        assertTrue(answer.isFictionalSimulation)
    }

    @Test
    fun `original persona answer is not labelled public simulation`() {
        val original = persona.copy(id = "original", kind = PersonaKind.ORIGINAL)
        val answer = PersonaAnswerEngine.answer(
            persona = original,
            questionId = "q1",
            questionText = "Un choix ?",
            options = listOf("A", "B"),
            seed = 9L,
        )

        assertTrue(!answer.isFictionalSimulation)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `empty options are rejected instead of inventing an answer`() {
        PersonaAnswerEngine.answer(
            persona = persona,
            questionId = "q1",
            questionText = "Un choix ?",
            options = emptyList(),
            seed = 9L,
        )
    }

    @Test
    fun `seed participates in deterministic tie breaking`() {
        val options = (1..12).map { "Option $it" }
        val answers = (1L..40L).map { seed ->
            PersonaAnswerEngine.answer(
                persona = persona,
                questionId = "neutral",
                questionText = "Choisis une option",
                options = options,
                seed = seed,
            ).option
        }.toSet()

        assertNotEquals(1, answers.size)
    }
}
