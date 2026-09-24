package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsNavigationTest {
    @Test
    fun settings_open_from_home_and_return_home() {
        val vm = GameViewModel(SavedStateHandle())
        vm.openSettings()
        assertEquals(AppScreen.SETTINGS.name, vm.screenName.value)
        vm.goBackHome()
        assertEquals(AppScreen.HOME.name, vm.screenName.value)
    }
}
