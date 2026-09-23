package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.Vote

object SessionCodec {
    private const val SEPARATOR = "\u001F"

    fun encodeVote(vote: Vote): String =
        vote.questionId + SEPARATOR + vote.answer

    fun decodeVote(value: String): Vote? {
        val index = value.indexOf(SEPARATOR)
        if (index <= 0 || index >= value.lastIndex) return null
        return Vote(
            questionId = value.substring(0, index),
            answer = value.substring(index + SEPARATOR.length)
        )
    }

    fun encodeVotes(votes: List<Vote>): List<String> = votes.map(::encodeVote)

    fun decodeVotes(values: List<String>): List<Vote> = values.mapNotNull(::decodeVote)
}
