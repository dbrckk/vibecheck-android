package com.vibecheck.app.ui.screens

import org.junit.Assert.assertEquals
import org.junit.Test

class ResultFeedbackPolicyTest {
    @Test
    fun winner_reveal_uses_confirm_feedback() {
        assertEquals(ResultFeedback.CONFIRM, resultRevealFeedback(challengeWon = false))
    }

    @Test
    fun challenge_win_uses_stronger_success_feedback() {
        assertEquals(ResultFeedback.SUCCESS, resultRevealFeedback(challengeWon = true))
    }

    @Test
    fun solo_result_does_not_pollute_local_stats_or_create_multiplayer_challenge() {
        val policy = resultPolicy(isSoloSession = true)
        assertEquals(false, policy.recordLocalStats)
        assertEquals(false, policy.allowChallengeShare)
    }

    @Test
    fun multiplayer_result_keeps_stats_and_challenge_share() {
        val policy = resultPolicy(isSoloSession = false)
        assertEquals(true, policy.recordLocalStats)
        assertEquals(true, policy.allowChallengeShare)
    }

    @Test
    fun solo_result_uses_dedicated_copy() {
        val copy = resultCopy(
            isSoloSession = true,
            knowsMe = false,
            modeTitle = "Qui de nous ?"
        )
        assertEquals("CASTING SIMULÉ", copy.badge)
        assertEquals("du casting simulé • Qui de nous ?", copy.scoreLabel)
        assertEquals("Rejouer avec ce casting", copy.replayLabel)
        assertEquals("Accueil", copy.homeLabel)
    }
}
