package com.vibecheck.app.domain.solo

import com.vibecheck.app.data.PersonaCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonaAnswerEngineTest {
    private val engine = PersonaAnswerEngine()
    private val persona = PersonaCatalog.all.first()
    private val options = listOf(
        PersonaOption("safe", "Rester prudent", PersonaTraits(20, 10, 70, 30, 20, 30, 5, 95)),
        PersonaOption("bold", "Tenter le coup", PersonaTraits(60, 95, 30, 70, 80, 45, 75, 20)),
        PersonaOption("social", "Demander au groupe", PersonaTraits(55, 45, 85, 100, 35, 55, 25, 55))
    )

    @Test
    fun same_persona_question_seed_and_options_are_deterministic() {
        val first = engine.answer(persona, "question_42", options, 12345L)
        val second = engine.answer(persona, "question_42", options, 12345L)
        assertEquals(first, second)
    }

    @Test
    fun answer_always_selects_one_of_the_provided_options() {
        repeat(100) { seed ->
            val answer = engine.answer(persona, "q$seed", options, seed.toLong())
            assertTrue(options.any { it.id == answer.optionId })
            assertTrue(answer.score in 0..100)
        }
    }

    @Test
    fun public_persona_answer_is_explicitly_a_fictional_simulation() {
        val publicPersona = PersonaCatalog.all.first { it.kind == PersonaKind.PUBLIC_SIMULATION }
        assertTrue(engine.answer(publicPersona, "q", options, 9L).isFictionalSimulation)
    }

    @Test
    fun original_character_is_not_labelled_as_public_figure_simulation() {
        val original = PersonaCatalog.all.first { it.kind == PersonaKind.ORIGINAL }
        assertTrue(!engine.answer(original, "q", options, 9L).isFictionalSimulation)
    }

    @Test
    fun strongly_opposed_trait_profiles_can_choose_different_options() {
        val cautious = Persona("cautious", "Cautious", PersonaGender.MAN, PersonaKind.ORIGINAL, "Calme", "Test", PersonaTraits(10, 0, 70, 20, 10, 20, 0, 100))
        val daring = Persona("daring", "Daring", PersonaGender.WOMAN, PersonaKind.ORIGINAL, "Audacieuse", "Test", PersonaTraits(70, 100, 30, 70, 90, 40, 90, 5))
        val a = engine.answer(cautious, "risk", options, 77L)
        val b = engine.answer(daring, "risk", options, 77L)
        assertNotEquals(a.optionId, b.optionId)
    }

    @Test
    fun empty_options_fall_back_safely_without_fabricating_a_claim() {
        val answer = engine.answer(persona, "unknown", emptyList(), 1L)
        assertEquals(null, answer.optionId)
        assertEquals("Pas de réponse disponible", answer.reaction)
    }
}
