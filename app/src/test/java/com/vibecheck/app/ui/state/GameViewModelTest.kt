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
    fun challenge_seed_survives_start_and_replay() {
        val viewModel = GameViewModel(SavedStateHandle())
        val seed = 123456789L

        viewModel.acceptChallenge(Challenge(GameMode.WHO_OF_US, 64, seed = seed))
        viewModel.startGame()
        assertEquals(seed, viewModel.sessionSeed.value)

        viewModel.replay()
        assertEquals(seed, viewModel.sessionSeed.value)
    }

    @Test
    fun red_green_starts_immediately_without_player_setup() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectMode(GameMode.RED_GREEN)

        assertEquals(GameMode.RED_GREEN.name, viewModel.modeName.value)
        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
    }

    @Test
    fun red_green_challenge_starts_immediately_and_keeps_target() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.acceptChallenge(Challenge(GameMode.RED_GREEN, 75))

        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(75, viewModel.challengeTarget.value)
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
    fun abandoning_game_clears_progress_and_returns_home() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectMode(GameMode.RED_GREEN)
        viewModel.answer("q1", "Green Flag", isLastQuestion = false)
        viewModel.abandonGame()

        assertEquals(AppScreen.HOME.name, viewModel.screenName.value)
        assertEquals(0, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isEmpty())
        assertEquals(GameViewModel.NO_CHALLENGE, viewModel.challengeTarget.value)
    }

    @Test
    fun intermediate_answer_advances_question() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.answer("q1", "Alice", isLastQuestion = false)

        assertEquals(1, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isNotEmpty())
    }
}
