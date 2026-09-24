package com.vibecheck.app.domain.solo

import kotlin.math.abs
import kotlin.math.roundToInt

class PersonaAnswerEngine {
    fun answer(
        persona: Persona,
        questionId: String,
        options: List<PersonaOption>,
        sessionSeed: Long
    ): PersonaAnswer {
        if (options.isEmpty()) {
            return PersonaAnswer(
                personaId = persona.id,
                optionId = null,
                score = 0,
                reaction = "Pas de réponse disponible",
                isFictionalSimulation = persona.isFictionalSimulation
            )
        }

        val ranked = options.map { option ->
            val compatibility = compatibility(persona.traits, option.traits)
            val variation = stableVariation(persona.id, questionId, option.id, sessionSeed)
            val score = (compatibility + variation).coerceIn(0.0, 100.0)
            option to score
        }
        val winner = ranked.maxWithOrNull(
            compareBy<Pair<PersonaOption, Double>> { it.second }
                .thenByDescending { it.first.id }
        ) ?: error("options cannot be empty")

        return PersonaAnswer(
            personaId = persona.id,
            optionId = winner.first.id,
            score = winner.second.roundToInt().coerceIn(0, 100),
            reaction = reactionFor(persona, winner.second, questionId, sessionSeed),
            isFictionalSimulation = persona.isFictionalSimulation
        )
    }

    private fun compatibility(persona: PersonaTraits, option: PersonaTraits): Double {
        val p = persona.values()
        val o = option.values()
        val averageDistance = p.zip(o).sumOf { (a, b) -> abs(a - b).toDouble() } / p.size
        return 100.0 - averageDistance
    }

    /** Small deterministic nudge: enough to vary close calls, never enough to erase personality. */
    private fun stableVariation(personaId: String, questionId: String, optionId: String, seed: Long): Double {
        var hash = 1125899906842597L
        "$personaId|$questionId|$optionId|$seed".forEach { char -> hash = 31L * hash + char.code }
        val bucket = ((hash xor (hash ushr 32)) and 0x7fffffffL) % 1201L
        return bucket / 100.0 - 6.0 // [-6, +6]
    }

    private fun reactionFor(persona: Persona, score: Double, questionId: String, seed: Long): String {
        val confident = score >= 78.0
        val playful = persona.traits.humor >= 75 || persona.traits.chaos >= 75
        val templates = when {
            confident && playful -> listOf("Je pars là-dessus.", "Celui-là, sans hésiter.", "Ça me ressemble bien.")
            confident -> listOf("C'est mon choix.", "Je choisis celui-ci.", "Ça me paraît juste.")
            playful -> listOf("Allez, tentons ça.", "Pourquoi pas celui-là ?", "Je tente ce choix.")
            else -> listOf("Je vais choisir celui-ci.", "C'est celui qui me correspond le mieux.", "Je pars sur cette réponse.")
        }
        val index = stableIndex("${persona.id}|$questionId|$seed|reaction", templates.size)
        return templates[index]
    }

    private fun stableIndex(value: String, size: Int): Int {
        var hash = 17L
        value.forEach { hash = 37L * hash + it.code }
        return ((hash xor (hash ushr 32)) and 0x7fffffffL).rem(size.toLong()).toInt()
    }
}
