package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.solo.PersonaKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloPartyViewModelTest {
    @Test fun `selection requires at least two companions before start`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.toggle(PersonaCatalog.all.first().id); assertFalse(vm.canStart.value); assertTrue(vm.validationMessage.value.contains("2")) }
    @Test fun `selection accepts two through seven companions`() { val vm=SoloPartyViewModel(SavedStateHandle()); PersonaCatalog.all.take(7).forEach{vm.toggle(it.id)}; assertTrue(vm.canStart.value); assertEquals(7,vm.selectedIds.value.size) }
    @Test fun `eighth companion is rejected without losing current selection`() { val vm=SoloPartyViewModel(SavedStateHandle()); PersonaCatalog.all.take(8).forEach{vm.toggle(it.id)}; assertEquals(7,vm.selectedIds.value.size) }
    @Test fun `tapping selected companion deselects it`() { val vm=SoloPartyViewModel(SavedStateHandle()); val p=PersonaCatalog.all.first(); vm.toggle(p.id); vm.toggle(p.id); assertTrue(vm.selectedIds.value.isEmpty()) }
    @Test fun `filter switches between public simulations and originals`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.setKindFilter(PersonaKind.ORIGINAL); assertTrue(vm.visiblePersonas.value.all{it.kind==PersonaKind.ORIGINAL}) }
    @Test fun `search matches persona display name case insensitively`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.setQuery("zend"); assertEquals(listOf("Zendaya"),vm.visiblePersonas.value.map{it.displayName}) }
    @Test fun `auto compose selects a valid mixed party reproducibly`() { val a=SoloPartyViewModel(SavedStateHandle()); val b=SoloPartyViewModel(SavedStateHandle()); a.autoCompose(42L,5); b.autoCompose(42L,5); assertEquals(a.selectedIds.value,b.selectedIds.value); assertTrue(a.canStart.value) }
    @Test fun `selected companion ids survive viewmodel recreation`() { val state=SavedStateHandle(); val a=SoloPartyViewModel(state); PersonaCatalog.all.take(3).forEach{a.toggle(it.id)}; assertEquals(a.selectedIds.value,SoloPartyViewModel(state).selectedIds.value) }

    @Test
    fun `solo player uses localized default and editable name survives recreation`() {
        val state = SavedStateHandle()
        val first = SoloPartyViewModel(state)
        assertEquals(SoloPartyViewModel.DEFAULT_PLAYER_NAME, first.playerName.value)
        first.setPlayerName("Alex")
        assertEquals("Alex", SoloPartyViewModel(state).playerName.value)
    }

    @Test
    fun `blank solo player name normalizes back to localized default`() {
        val vm = SoloPartyViewModel(SavedStateHandle())
        vm.setPlayerName("   ")
        assertEquals(SoloPartyViewModel.DEFAULT_PLAYER_NAME, vm.playerName.value)
    }
}
