package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
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
    @Test
    fun full_group_session_reaches_result_after_eight_answers() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectMode(GameMode.WHO_OF_US)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()

        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)

        repeat(7) { index ->
            viewModel.answer("q" + (index + 1), "Alice", isLastQuestion = false)
            assertEquals(index + 1, viewModel.questionIndex.value)
            assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        }

        viewModel.answer("q8", "Bob", isLastQuestion = true)

        assertEquals(AppScreen.RESULT.name, viewModel.screenName.value)
        assertEquals(8, SessionCodec.decodeVotes(viewModel.savedVotes.value).size)
    }

    @Test
    fun replay_resets_votes_and_question_but_keeps_players() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectMode(GameMode.WHO_OF_US)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()
        viewModel.answer("q1", "Alice", isLastQuestion = true)

        viewModel.replay()

        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(0, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isEmpty())
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)
    }

    @Test
    fun returning_home_from_result_clears_session_but_keeps_group() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectMode(GameMode.WHO_OF_US)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()
        viewModel.answer("q1", "Alice", isLastQuestion = true)

        viewModel.goHome()

        assertEquals(AppScreen.HOME.name, viewModel.screenName.value)
        assertEquals(0, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isEmpty())
        assertEquals(GameViewModel.NO_CHALLENGE, viewModel.challengeTarget.value)
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)
    }
    @Test
    fun saved_state_restores_active_session_after_recreation() {
        val state = SavedStateHandle()
        val first = GameViewModel(state)

        first.selectMode(GameMode.WHO_OF_US)
        first.addPlayer("Alice")
        first.addPlayer("Bob")
        first.startGame()
        first.answer("q1", "Alice", isLastQuestion = false)

        val restored = GameViewModel(state)

        assertEquals(AppScreen.GAME.name, restored.screenName.value)
        assertEquals(GameMode.WHO_OF_US.name, restored.modeName.value)
        assertEquals(1, restored.questionIndex.value)
        assertEquals(listOf("Alice", "Bob"), restored.players.value)
        assertEquals(1, SessionCodec.decodeVotes(restored.savedVotes.value).size)
        assertEquals(first.sessionSeed.value, restored.sessionSeed.value)
    }

    @Test
    fun new_challenge_replaces_active_session_atomically() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.selectMode(GameMode.WHO_OF_US)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()
        viewModel.answer("q1", "Alice", isLastQuestion = false)

        viewModel.acceptChallenge(Challenge(GameMode.RED_GREEN, 70, seed = 4242L))

        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(GameMode.RED_GREEN.name, viewModel.modeName.value)
        assertEquals(0, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isEmpty())
        assertEquals(70, viewModel.challengeTarget.value)
        assertEquals(4242L, viewModel.sessionSeed.value)
    }

    @Test
    fun know_me_scores_each_guesser_and_advances_after_everyone_guesses() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.selectMode(GameMode.KNOWS_ME)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.addPlayer("Chloe")
        viewModel.startGame()

        viewModel.setKnowMeSecret("Mer")
        assertEquals("Mer", viewModel.knowSecretAnswer.value)
        assertEquals(0, viewModel.knowGuesserIndex.value)
        assertTrue(viewModel.knowHandoffPending.value)
        viewModel.confirmKnowMeHandoff()
        assertFalse(viewModel.knowHandoffPending.value)

        viewModel.submitKnowMeGuess("Mer", isLastQuestion = false)
        assertEquals(1, viewModel.knowGuesserIndex.value)
        assertEquals(listOf(1, 0), viewModel.knowScores.value)
        assertTrue(viewModel.knowHandoffPending.value)
        viewModel.confirmKnowMeHandoff()
        assertEquals(0, viewModel.questionIndex.value)

        viewModel.submitKnowMeGuess("Montagne", isLastQuestion = false)
        assertEquals(1, viewModel.questionIndex.value)
        assertEquals("", viewModel.knowSecretAnswer.value)
        assertEquals(0, viewModel.knowGuesserIndex.value)
        assertTrue(viewModel.knowHandoffPending.value)
        assertEquals(listOf(1, 0), viewModel.knowScores.value)
    }

    @Test
    fun know_me_last_round_reaches_result_and_survives_recreation() {
        val state = SavedStateHandle()
        val viewModel = GameViewModel(state)
        viewModel.selectMode(GameMode.KNOWS_ME)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()
        viewModel.setKnowMeSecret("Mer")
        viewModel.submitKnowMeGuess("Mer", isLastQuestion = true)

        val restored = GameViewModel(state)
        assertEquals(AppScreen.RESULT.name, restored.screenName.value)
        assertEquals(listOf(1), restored.knowScores.value)
        assertEquals("Mer", restored.knowSecretAnswer.value)
    }

    @Test
    fun abandoning_know_me_clears_private_answer_and_scores() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.selectMode(GameMode.KNOWS_ME)
        viewModel.addPlayer("Alice")
        viewModel.addPlayer("Bob")
        viewModel.startGame()
        viewModel.setKnowMeSecret("Mer")
        viewModel.submitKnowMeGuess("Mer", isLastQuestion = false)

        viewModel.abandonGame()

        assertEquals("", viewModel.knowSecretAnswer.value)
        assertTrue(viewModel.knowScores.value.isEmpty())
        assertEquals(0, viewModel.knowGuesserIndex.value)
        assertFalse(viewModel.knowHandoffPending.value)
    }
    @Test
    fun intensity_is_persisted_and_challenge_can_lock_it() {
        val state = SavedStateHandle()
        val viewModel = GameViewModel(state)

        viewModel.selectIntensity(GameIntensity.SAVAGE)
        assertEquals(GameIntensity.SAVAGE.name, viewModel.intensityName.value)

        viewModel.acceptChallenge(
            Challenge(
                mode = GameMode.WHO_OF_US,
                targetPercent = 70,
                seed = 99L,
                intensity = GameIntensity.CHILL,
                pack = GamePack.MIX
            )
        )

        assertEquals(GameIntensity.CHILL.name, viewModel.intensityName.value)
        assertFalse(viewModel.legacyChallenge.value)

        viewModel.selectIntensity(GameIntensity.SAVAGE)
        assertEquals(GameIntensity.CHILL.name, viewModel.intensityName.value)
    }

    @Test
    fun legacy_challenge_is_marked_for_full_pool_replay() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.acceptChallenge(
            Challenge(GameMode.WHO_OF_US, targetPercent = 60, seed = 1L)
        )

        assertTrue(viewModel.legacyChallenge.value)
        assertEquals(GameIntensity.NORMAL.name, viewModel.intensityName.value)
    }
    @Test
    fun pack_is_persisted_and_locked_by_challenge() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.selectPack(GamePack.DEEP)
        assertEquals(GamePack.DEEP.name, viewModel.packName.value)

        viewModel.acceptChallenge(
            Challenge(
                mode = GameMode.WHO_OF_US,
                targetPercent = 70,
                seed = 44L,
                intensity = GameIntensity.NORMAL,
                pack = GamePack.FRIENDS
            )
        )

        assertEquals(GamePack.FRIENDS.name, viewModel.packName.value)
        viewModel.selectPack(GamePack.CHAOS)
        assertEquals(GamePack.FRIENDS.name, viewModel.packName.value)
    }
    @Test
    fun persisted_group_restores_only_when_session_has_no_players() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.restorePlayers(listOf("Alice", "Bob"))
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)

        viewModel.restorePlayers(listOf("Chloe", "Dan"))
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)
    }
    @Test
    fun v2_challenge_preserves_intensity_with_mix_pack() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.acceptChallenge(
            Challenge(
                mode = GameMode.WHO_OF_US,
                targetPercent = 65,
                seed = 12L,
                intensity = GameIntensity.SAVAGE
            )
        )

        assertEquals(GameIntensity.SAVAGE.name, viewModel.intensityName.value)
        assertEquals(GamePack.MIX.name, viewModel.packName.value)
        assertFalse(viewModel.legacyChallenge.value)
    }
    @Test
    fun quick_start_with_valid_group_opens_game_immediately() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.restorePlayers(listOf("Alice", "Bob"))

        viewModel.quickStartMode(GameMode.WHO_OF_US)

        assertEquals(GameMode.WHO_OF_US.name, viewModel.modeName.value)
        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(0, viewModel.questionIndex.value)
        assertTrue(viewModel.savedVotes.value.isEmpty())
    }

    @Test
    fun quick_start_without_enough_players_falls_back_to_setup() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.restorePlayers(listOf("Alice"))

        viewModel.quickStartMode(GameMode.MOST_LIKELY)

        assertEquals(AppScreen.PLAYERS.name, viewModel.screenName.value)
    }

    @Test
    fun red_green_quick_start_does_not_require_players() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.quickStartMode(GameMode.RED_GREEN)

        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
    }

    @Test
    fun edit_players_keeps_pack_and_intensity_configuration() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.selectPack(GamePack.DEEP)
        viewModel.selectIntensity(GameIntensity.SAVAGE)

        viewModel.editPlayers()

        assertEquals(AppScreen.PLAYERS.name, viewModel.screenName.value)
        assertEquals(GamePack.DEEP.name, viewModel.packName.value)
        assertEquals(GameIntensity.SAVAGE.name, viewModel.intensityName.value)
    }
    @Test
    fun session_instance_survives_viewmodel_recreation() {
        val state = SavedStateHandle()
        val first = GameViewModel(state)
        first.restorePlayers(listOf("Alice", "Bob"))
        first.quickStartMode(GameMode.WHO_OF_US)
        val instance = first.sessionInstanceId.value

        val restored = GameViewModel(state)

        assertEquals(instance, restored.sessionInstanceId.value)
    }

    @Test
    fun replay_creates_a_new_session_instance_even_for_same_challenge_seed() {
        val state = SavedStateHandle()
        val viewModel = GameViewModel(state)
        viewModel.restorePlayers(listOf("Alice", "Bob"))
        viewModel.acceptChallenge(
            Challenge(
                mode = GameMode.WHO_OF_US,
                targetPercent = 50,
                seed = 123L,
                intensity = GameIntensity.NORMAL,
                pack = GamePack.MIX
            )
        )
        val firstInstance = viewModel.sessionInstanceId.value

        viewModel.replay()

        assertEquals(123L, viewModel.sessionSeed.value)
        assertNotEquals(firstInstance, viewModel.sessionInstanceId.value)
    }
    @Test
    fun leaderboard_opens_and_returns_home() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.openLeaderboard()
        assertEquals(AppScreen.LEADERBOARD.name, viewModel.screenName.value)

        viewModel.goHome()
        assertEquals(AppScreen.HOME.name, viewModel.screenName.value)
    }
}
