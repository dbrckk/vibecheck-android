package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloNavigationTest {
    @Test
    fun open_solo_navigates_to_casting_and_selection_persists() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        assertEquals(AppScreen.SOLO_PARTY.name, vm.screenName.value)

        vm.setSoloParty(listOf("public_taylor_swift", "original_nova"))
        assertEquals(listOf("public_taylor_swift", "original_nova"), vm.soloPersonaIds.value)
        assertTrue(vm.isSoloSession.value)
    }

    @Test
    fun leaving_solo_casting_returns_home_and_clears_solo_state() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        vm.setSoloParty(listOf("public_taylor_swift", "original_nova"))
        vm.goBackHome()

        assertEquals(AppScreen.HOME.name, vm.screenName.value)
        assertFalse(vm.isSoloSession.value)
        assertTrue(vm.soloPersonaIds.value.isEmpty())
    }

    @Test
    fun valid_solo_party_can_start_game_and_survive_recreation() {
        val state = SavedStateHandle()
        val vm = GameViewModel(state)
        vm.openSoloParty()
        vm.setSoloParty(listOf("public_taylor_swift", "original_nova"))
        vm.startSoloGame()

        assertEquals(AppScreen.GAME.name, vm.screenName.value)
        assertTrue(vm.isSoloSession.value)
        assertEquals(GameViewModel.DEFAULT_SOLO_MODE.name, vm.modeName.value)

        val restored = GameViewModel(state)
        assertEquals(AppScreen.GAME.name, restored.screenName.value)
        assertTrue(restored.isSoloSession.value)
        assertEquals(listOf("public_taylor_swift", "original_nova"), restored.soloPersonaIds.value)
    }

    @Test
    fun solo_game_does_not_start_with_fewer_than_two_personas() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        vm.setSoloParty(listOf("public_taylor_swift"))
        vm.startSoloGame()
        assertEquals(AppScreen.SOLO_PARTY.name, vm.screenName.value)
    }

    @Test
    fun abandoning_solo_game_clears_cast_before_next_session() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        vm.setSoloParty(listOf("public_taylor_swift", "original_nova"))
        vm.startSoloGame()
        vm.abandonGame()

        assertEquals(AppScreen.HOME.name, vm.screenName.value)
        assertFalse(vm.isSoloSession.value)
        assertTrue(vm.soloPersonaIds.value.isEmpty())
    }

    @Test
    fun finishing_solo_and_going_home_clears_cast_before_next_session() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        vm.setSoloParty(listOf("public_taylor_swift", "original_nova"))
        vm.startSoloGame()
        vm.goHome()

        assertEquals(AppScreen.HOME.name, vm.screenName.value)
        assertFalse(vm.isSoloSession.value)
        assertTrue(vm.soloPersonaIds.value.isEmpty())
    }
}
