package com.vibecheck.app.ui.screens

internal enum class ResultFeedback {
    CONFIRM,
    SUCCESS
}

internal data class ResultPolicy(
    val recordLocalStats: Boolean,
    val allowChallengeShare: Boolean
)

internal fun resultRevealFeedback(challengeWon: Boolean): ResultFeedback =
    if (challengeWon) ResultFeedback.SUCCESS else ResultFeedback.CONFIRM

internal fun resultPolicy(isSoloSession: Boolean): ResultPolicy =
    if (isSoloSession) {
        ResultPolicy(recordLocalStats = false, allowChallengeShare = false)
    } else {
        ResultPolicy(recordLocalStats = true, allowChallengeShare = true)
    }
