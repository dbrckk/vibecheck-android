package com.vibecheck.app.domain.solo

import kotlin.math.abs

object PersonaAnswerEngine {
    fun answer(
        persona: Persona,
        questionId: String,
        questionText: String,
        options: List<String>,
        seed: Long,
    ): PersonaAnswer {
        require(options.isNotEmpty()) { "Persona answers require at least one option" }

        val normalizedQuestion = questionText.lowercase()
        val tendency = tendencyScore(persona.traits, normalizedQuestion)
        val scored = options.mapIndexed { index, option ->
            val noise = stableNoise(
                personaId = persona.id,
                questionId = questionId,
                option = option,
                seed = seed,
                index = index,
            )
            option to (noise + tendencyModifier(tendency, index, options.size))
        }
        val ranked = scored.sortedWith(
            compareByDescending<Pair<String, Int>> { it.second }
                .thenBy { it.first }
        )
        val winner = ranked.first()
        val runnerUp = ranked.getOrNull(1)
        val gap = if (runnerUp == null) 100 else abs(winner.second - runnerUp.second)
        val confidence = (55 + gap / 3).coerceIn(55, 96)

        return PersonaAnswer(
            personaId = persona.id,
            option = winner.first,
            confidence = confidence,
            isFictionalSimulation = persona.isFictionalSimulation,
        )
    }

    private fun tendencyScore(traits: PersonaTraits, question: String): Int {
        var score = (
            traits.boldness +
                traits.humor +
                traits.sociability +
                traits.competitiveness +
                traits.chaos -
                traits.deliberation
            ) / 5

        if (question.containsAny("risqu", "osé", "ose", "audac", "spontan", "improvis")) {
            score += (traits.boldness + traits.chaos - traits.deliberation) / 4
        }
        if (question.containsAny("amour", "romanti", "date", "couple", "crush")) {
            score += (traits.romanticism + traits.empathy - 100) / 4
        }
        if (question.containsAny("ami", "groupe", "soirée", "soiree", "social")) {
            score += (traits.sociability + traits.humor - 100) / 4
        }
        if (question.containsAny("gagn", "compét", "compet", "défi", "defi")) {
            score += (traits.competitiveness - 50) / 2
        }
        if (question.containsAny("aider", "écout", "ecout", "soutien", "gentil")) {
            score += (traits.empathy - 50) / 2
        }

        return score.coerceIn(-100, 100)
    }

    private fun tendencyModifier(tendency: Int, index: Int, optionCount: Int): Int {
        if (optionCount <= 1) return 0
        val centered = (index * 200 / (optionCount - 1)) - 100
        return tendency * centered / 100
    }

    private fun stableNoise(
        personaId: String,
        questionId: String,
        option: String,
        seed: Long,
        index: Int,
    ): Int {
        var value = seed xor 0x9E3779B97F4A7C15UL.toLong()
        value = mix(value xor personaId.hashCode().toLong())
        value = mix(value xor questionId.hashCode().toLong())
        value = mix(value xor option.hashCode().toLong())
        value = mix(value xor index.toLong())
        return ((value ushr 1) % 201L).toInt() - 100
    }

    private fun mix(input: Long): Long {
        var value = input
        value = (value xor (value ushr 30)) * 0xBF58476D1CE4E5B9UL.toLong()
        value = (value xor (value ushr 27)) * 0x94D049BB133111EBUL.toLong()
        return value xor (value ushr 31)
    }

    private fun String.containsAny(vararg needles: String): Boolean =
        needles.any(::contains)
}
