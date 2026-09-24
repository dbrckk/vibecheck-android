package com.vibecheck.app.ui.screens

internal enum class ResultFeedback {
    CONFIRM,
    SUCCESS
}

internal fun resultRevealFeedback(challengeWon: Boolean): ResultFeedback =
    if (challengeWon) ResultFeedback.SUCCESS else ResultFeedback.CONFIRM
