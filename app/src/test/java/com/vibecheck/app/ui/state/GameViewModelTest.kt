package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    @Test
    fun selecting_mode_opens_player_setup_and_clears_challenge() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.acceptChallenge(Challenge(GameMode.RED_GREEN, 72))
        viewModel.selectMode(GameMode.MOST_LIKELY)

        assertEquals(GameMode.MOST_LIKELY.name, viewModel.modeName.value)
        assertEquals(AppScreen.PLAYERS.name, viewModel.screenName.value)
        assertEquals(GameViewModel.NO_CHALLENGE, viewModel.challengeTarget.value)
    }

    @Test
    fun incoming_challenge_preserves_target_and_mode() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.acceptChallenge(Challenge(GameMode.WHO_OF_US, 64))

        assertEquals(GameMode.WHO_OF_US.name, viewModel.modeName.value)
        assertEquals(64, viewModel.challengeTarget.value)
        assertEquals(AppScreen.PLAYERS.name, viewModel.screenName.value)
    }

    @Test
    fun final_answer_opens_result_and_saves_vote() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.answer("q1", "Alice", isLastQuestion = true)

        assertEquals(AppScreen.RESULT.name, viewModel.screenName.value)
        val votes = SessionCodec.decodeVotes(viewModel.savedVotes.value)
        assertEquals(1, votes.size)
        assertEquals("Alice", votes.first().answer)
    }

    @Test
    fun intermediate_answer_advances_question() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.answer("q1", "Alice", isLastQuestion = false)

        assertEquals(1, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isNotEmpty())
    }
}
