package com.vibecheck.app.ui.screens

internal enum class ResultFeedback {
    CONFIRM,
    SUCCESS
}

internal data class ResultPolicy(
    val recordLocalStats: Boolean,
    val allowChallengeShare: Boolean
)

internal data class ResultCopy(
    val badge: String,
    val scoreLabel: String,
    val replayLabel: String,
    val homeLabel: String
)

internal fun resultRevealFeedback(challengeWon: Boolean): ResultFeedback =
    if (challengeWon) ResultFeedback.SUCCESS else ResultFeedback.CONFIRM

internal fun resultPolicy(isSoloSession: Boolean): ResultPolicy =
    if (isSoloSession) {
        ResultPolicy(recordLocalStats = false, allowChallengeShare = false)
    } else {
        ResultPolicy(recordLocalStats = true, allowChallengeShare = true)
    }

internal fun resultCopy(
    isSoloSession: Boolean,
    knowsMe: Boolean,
    modeTitle: String
): ResultCopy =
    if (isSoloSession) {
        ResultCopy(
            badge = "CASTING SIMULÉ",
            scoreLabel = "du casting simulé • $modeTitle",
            replayLabel = "Rejouer avec ce casting",
            homeLabel = "Accueil"
        )
    } else {
        ResultCopy(
            badge = if (knowsMe) "QUI CONNAÎT LE MIEUX ?" else "LE GROUPE A PARLÉ",
            scoreLabel = if (knowsMe) "de bonnes réponses • $modeTitle" else "des réponses • $modeTitle",
            replayLabel = "Rejouer",
            homeLabel = "Modes"
        )
    }
