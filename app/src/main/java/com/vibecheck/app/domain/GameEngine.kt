package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameResult
import com.vibecheck.app.domain.model.Vote

object GameEngine {
    fun result(votes: List<Vote>): GameResult {
        if (votes.isEmpty()) return GameResult("Personne", 0, 0)
        val winner = votes.groupingBy { it.answer }.eachCount().entries
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .first()
        return GameResult(winner.key, winner.value, votes.size)
    }
}
