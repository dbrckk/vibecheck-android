package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameResult
import com.vibecheck.app.domain.model.Vote

object GameEngine {
    fun ranking(votes: List<Vote>): List<Pair<String, Int>> =
        votes.groupingBy { it.answer }
            .eachCount()
            .entries
            .sortedWith(
                compareByDescending<Map.Entry<String, Int>> { it.value }
                    .thenBy { it.key }
            )
            .map { it.key to it.value }

    fun result(votes: List<Vote>): GameResult {
        if (votes.isEmpty()) return GameResult("Personne", 0, 0)
        val winner = ranking(votes).first()
        return GameResult(winner.first, winner.second, votes.size)
    }
}
