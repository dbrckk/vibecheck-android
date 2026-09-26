package com.vibecheck.app.domain.solo

import com.vibecheck.app.domain.model.Question

data class SoloPersonaVote(
    val voterId: String,
    val targetPersonaId: String,
    val reaction: String,
    val isFictionalSimulation: Boolean
)

data class SoloRoundResult(
    val questionId: String,
    val votes: List<SoloPersonaVote>,
    val winnerPersonaId: String?
)

class SoloRoundResolver(
    private val answerEngine: PersonaAnswerEngine = PersonaAnswerEngine()
) {
    fun resolve(
        question: Question,
        cast: List<Persona>,
        sessionSeed: Long
    ): SoloRoundResult {
        if (cast.isEmpty()) {
            return SoloRoundResult(question.id, emptyList(), null)
        }

        val options = cast.map { target ->
            PersonaOption(
                id = target.id,
                label = target.displayName,
                traits = target.traits
            )
        }

        val votes = cast.mapIndexed { index, voter ->
            val answer = answerEngine.answer(
                persona = voter,
                questionId = question.id,
                options = options,
                sessionSeed = sessionSeed + index * 7919L
            )
            SoloPersonaVote(
                voterId = voter.id,
                targetPersonaId = answer.optionId ?: cast.first().id,
                reaction = answer.reaction,
                isFictionalSimulation = answer.isFictionalSimulation
            )
        }

        val winner = votes
            .groupingBy { it.targetPersonaId }
            .eachCount()
            .entries
            .sortedWith(
                compareByDescending<Map.Entry<String, Int>> { it.value }
                    .thenBy { it.key }
            )
            .firstOrNull()
            ?.key

        return SoloRoundResult(
            questionId = question.id,
            votes = votes,
            winnerPersonaId = winner
        )
    }
}
