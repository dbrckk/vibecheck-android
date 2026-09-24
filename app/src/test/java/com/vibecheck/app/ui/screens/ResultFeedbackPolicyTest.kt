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
}
