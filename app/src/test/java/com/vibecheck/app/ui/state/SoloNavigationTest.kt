package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
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
    fun leaving_solo_casting_returns_home_without_marking_session_active() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSoloParty()
        vm.goBackHome()
        assertEquals(AppScreen.HOME.name, vm.screenName.value)
        assertTrue(!vm.isSoloSession.value)
    }
}
