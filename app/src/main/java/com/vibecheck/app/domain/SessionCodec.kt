package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.Vote

object SessionCodec {
    private const val LEGACY_SEPARATOR = "\u001F"
    private const val V2_PREFIX = "v2:"
    private const val LENGTH_SEPARATOR = ':'

    fun encodeVote(vote: Vote): String =
        V2_PREFIX + vote.questionId.length + LENGTH_SEPARATOR + vote.questionId + vote.answer

    fun decodeVote(value: String): Vote? =
        if (value.startsWith(V2_PREFIX)) decodeV2(value) else decodeLegacy(value)

    fun encodeVotes(votes: List<Vote>): List<String> = votes.map(::encodeVote)

    fun decodeVotes(values: List<String>): List<Vote> = values.mapNotNull(::decodeVote)

    private fun decodeV2(value: String): Vote? {
        val lengthEnd = value.indexOf(LENGTH_SEPARATOR, startIndex = V2_PREFIX.length)
        if (lengthEnd <= V2_PREFIX.length) return null

        val questionLength = value
            .substring(V2_PREFIX.length, lengthEnd)
            .toIntOrNull()
            ?.takeIf { it > 0 }
            ?: return null
        val questionStart = lengthEnd + 1
        val questionEnd = questionStart + questionLength
        if (questionEnd > value.length || questionEnd == value.length) return null

        val questionId = value.substring(questionStart, questionEnd)
        val answer = value.substring(questionEnd)
        if (questionId.isBlank() || answer.isBlank()) return null
        return Vote(questionId = questionId, answer = answer)
    }

    private fun decodeLegacy(value: String): Vote? {
        val index = value.indexOf(LEGACY_SEPARATOR)
        if (index <= 0 || index >= value.lastIndex) return null
        return Vote(
            questionId = value.substring(0, index),
            answer = value.substring(index + LEGACY_SEPARATOR.length)
        )
    }
}
