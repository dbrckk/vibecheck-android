package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.solo.PersonaAnswer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloGameViewModelTest {
    @Test
    fun `valid solo party starts game without replacing saved human group`() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.restorePlayers(listOf("Alice", "Bob"))

        viewModel.startSoloSession(
            personaIds = listOf("zendaya", "nova"),
            mode = GameMode.WHO_OF_US,
            seed = 42L,
        )

        assertTrue(viewModel.isSoloSession.value)
        assertEquals(listOf("zendaya", "nova"), viewModel.soloPersonaIds.value)
        assertEquals(listOf("Alice", "Bob"), viewModel.players.value)
        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)
        assertEquals(GameMode.WHO_OF_US.name, viewModel.modeName.value)
        assertEquals(42L, viewModel.sessionSeed.value)
    }

    @Test
    fun `solo session rejects fewer than two personas`() {
        val viewModel = GameViewModel(SavedStateHandle())

        viewModel.startSoloSession(
            personaIds = listOf("nova"),
            mode = GameMode.WHO_OF_US,
            seed = 42L,
        )

        assertFalse(viewModel.isSoloSession.value)
        assertEquals(AppScreen.HOME.name, viewModel.screenName.value)
    }

    @Test
    fun `solo round stores deterministic majority as one game vote`() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.startSoloSession(
            personaIds = listOf("zendaya", "nova", "milo"),
            mode = GameMode.WHO_OF_US,
            seed = 42L,
        )

        viewModel.submitSoloRound(
            questionId = "q1",
            answers = listOf(
                PersonaAnswer("zendaya", "Nova", 70, true),
                PersonaAnswer("nova", "Nova", 65, false),
                PersonaAnswer("milo", "Milo", 80, false),
            ),
            isLastQuestion = false,
        )

        val votes = SessionCodec.decodeVotes(viewModel.savedVotes.value)
        assertEquals(1, votes.size)
        assertEquals("Nova", votes.single().answer)
        assertEquals(1, viewModel.questionIndex.value)
    }

    @Test
    fun `solo final round opens result replay preserves party and home clears session mode`() {
        val viewModel = GameViewModel(SavedStateHandle())
        viewModel.startSoloSession(
            personaIds = listOf("zendaya", "nova"),
            mode = GameMode.WHO_OF_US,
            seed = 42L,
        )

        viewModel.submitSoloRound(
            questionId = "q8",
            answers = listOf(
                PersonaAnswer("zendaya", "Nova", 70, true),
                PersonaAnswer("nova", "Nova", 65, false),
            ),
            isLastQuestion = true,
        )

        assertEquals(AppScreen.RESULT.name, viewModel.screenName.value)

        viewModel.replay()
        assertTrue(viewModel.isSoloSession.value)
        assertEquals(listOf("zendaya", "nova"), viewModel.soloPersonaIds.value)
        assertEquals(AppScreen.GAME.name, viewModel.screenName.value)

        viewModel.goHome()
        assertFalse(viewModel.isSoloSession.value)
        assertTrue(viewModel.soloPersonaIds.value.isEmpty())
        assertEquals(AppScreen.HOME.name, viewModel.screenName.value)
    }

    @Test
    fun `active solo session survives viewmodel recreation`() {
        val state = SavedStateHandle()
        val first = GameViewModel(state)
        first.startSoloSession(
            personaIds = listOf("zendaya", "nova"),
            mode = GameMode.MOST_LIKELY,
            seed = 99L,
        )

        val restored = GameViewModel(state)

        assertTrue(restored.isSoloSession.value)
        assertEquals(listOf("zendaya", "nova"), restored.soloPersonaIds.value)
        assertEquals(GameMode.MOST_LIKELY.name, restored.modeName.value)
        assertEquals(AppScreen.GAME.name, restored.screenName.value)
        assertEquals(99L, restored.sessionSeed.value)
    }
}
