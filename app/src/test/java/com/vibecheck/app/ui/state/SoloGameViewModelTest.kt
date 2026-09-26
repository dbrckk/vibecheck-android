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
    private val publicIds = listOf("zendaya", "taylor_swift")

    @Test fun `valid solo party starts game with human participant awaiting answer`() {
        val vm = GameViewModel(SavedStateHandle())
        vm.startSoloSession(publicIds, GameMode.WHO_OF_US, 42L, "Alex")
        assertTrue(vm.isSoloSession.value)
        assertEquals("Alex", vm.soloPlayerName.value)
        assertEquals("", vm.soloPlayerAnswer.value)
        assertEquals(AppScreen.GAME.name, vm.screenName.value)
    }

    @Test fun `solo session rejects fewer than two personas`() {
        val vm = GameViewModel(SavedStateHandle())
        vm.startSoloSession(listOf("zendaya"), GameMode.WHO_OF_US, 42L, "Moi")
        assertFalse(vm.isSoloSession.value)
    }

    @Test fun `solo supports the three configured modes`() {
        listOf(GameMode.WHO_OF_US, GameMode.MOST_LIKELY, GameMode.RED_GREEN).forEach { mode ->
            val vm = GameViewModel(SavedStateHandle())
            vm.startSoloSession(publicIds, mode, 42L, "Moi")
            assertTrue("Expected solo session for $mode", vm.isSoloSession.value)
            assertEquals(mode.name, vm.modeName.value)
            assertEquals(AppScreen.GAME.name, vm.screenName.value)
        }
    }

    @Test fun `solo rejects knows me mode`() {
        val vm = GameViewModel(SavedStateHandle())
        vm.startSoloSession(publicIds, GameMode.KNOWS_ME, 42L, "Moi")
        assertFalse(vm.isSoloSession.value)
        assertEquals(AppScreen.HOME.name, vm.screenName.value)
    }

    @Test fun `human must answer before simulated round can be submitted`() {
        val vm = GameViewModel(SavedStateHandle())
        vm.startSoloSession(publicIds, GameMode.WHO_OF_US, 42L, "Moi")
        val answers = listOf(
            PersonaAnswer("zendaya", "Taylor Swift", 70, true),
            PersonaAnswer("taylor_swift", "Taylor Swift", 65, true),
        )
        vm.submitSoloRound("q1", answers, false)
        assertTrue(SessionCodec.decodeVotes(vm.savedVotes.value).isEmpty())
        vm.submitSoloPlayerAnswer("Moi")
        assertEquals("Moi", vm.soloPlayerAnswer.value)
        vm.submitSoloRound("q1", answers, false)
        assertEquals(1, SessionCodec.decodeVotes(vm.savedVotes.value).size)
        assertEquals("", vm.soloPlayerAnswer.value)
    }

    @Test fun `solo result combines human and simulated answers in global vote`() {
        val vm = GameViewModel(SavedStateHandle())
        vm.startSoloSession(publicIds, GameMode.WHO_OF_US, 42L, "Moi")
        vm.submitSoloPlayerAnswer("Moi")
        vm.submitSoloRound(
            "q1",
            listOf(
                PersonaAnswer("zendaya", "Taylor Swift", 70, true),
                PersonaAnswer("taylor_swift", "Taylor Swift", 65, true),
            ),
            false,
        )
        assertEquals("Taylor Swift", SessionCodec.decodeVotes(vm.savedVotes.value).single().answer)
    }

    @Test fun `active solo session survives recreation with player identity and mode`() {
        val state = SavedStateHandle()
        GameViewModel(state).startSoloSession(publicIds, GameMode.MOST_LIKELY, 99L, "Alex")
        val restored = GameViewModel(state)
        assertTrue(restored.isSoloSession.value)
        assertEquals("Alex", restored.soloPlayerName.value)
        assertEquals(GameMode.MOST_LIKELY.name, restored.modeName.value)
        assertEquals(99L, restored.sessionSeed.value)
    }
}
