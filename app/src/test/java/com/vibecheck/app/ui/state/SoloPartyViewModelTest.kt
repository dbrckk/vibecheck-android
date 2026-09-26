package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.solo.PersonaKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloPartyViewModelTest {
    @Test fun `selection requires at least two companions before start`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.toggle(PersonaCatalog.soloPublic.first().id); assertFalse(vm.canStart.value); assertTrue(vm.validationMessage.value.contains("2")) }
    @Test fun `selection accepts two through seven public companions`() { val vm=SoloPartyViewModel(SavedStateHandle()); PersonaCatalog.soloPublic.take(7).forEach{vm.toggle(it.id)}; assertTrue(vm.canStart.value); assertEquals(7,vm.selectedIds.value.size) }
    @Test fun `eighth companion is rejected without losing current selection`() { val vm=SoloPartyViewModel(SavedStateHandle()); PersonaCatalog.soloPublic.take(8).forEach{vm.toggle(it.id)}; assertEquals(7,vm.selectedIds.value.size) }
    @Test fun `tapping selected companion deselects it`() { val vm=SoloPartyViewModel(SavedStateHandle()); val p=PersonaCatalog.soloPublic.first(); vm.toggle(p.id); vm.toggle(p.id); assertTrue(vm.selectedIds.value.isEmpty()) }
    @Test fun `visible solo personas are always public simulations`() { val vm=SoloPartyViewModel(SavedStateHandle()); assertTrue(vm.visiblePersonas.value.isNotEmpty()); assertTrue(vm.visiblePersonas.value.all{it.kind==PersonaKind.PUBLIC_SIMULATION}) }
    @Test fun `original kind filter cannot expose fictional personas`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.setKindFilter(PersonaKind.ORIGINAL); assertTrue(vm.visiblePersonas.value.all{it.kind==PersonaKind.PUBLIC_SIMULATION}) }
    @Test fun `search matches public display name case insensitively`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.setQuery("zend"); assertEquals(listOf("Zendaya"),vm.visiblePersonas.value.map{it.displayName}) }
    @Test fun `auto compose selects only unique public figures reproducibly`() { val a=SoloPartyViewModel(SavedStateHandle()); val b=SoloPartyViewModel(SavedStateHandle()); a.autoCompose(42L,5); b.autoCompose(42L,5); assertEquals(a.selectedIds.value,b.selectedIds.value); assertEquals(5,a.selectedIds.value.distinct().size); assertTrue(a.selectedIds.value.all{ id -> PersonaCatalog.soloPublic.any{it.id==id} }); assertTrue(a.canStart.value) }
    @Test fun `restored unknown ids are discarded`() { val public=PersonaCatalog.soloPublic.first(); val state=SavedStateHandle(mapOf("solo_party_selected_ids" to listOf("legacy_original_persona",public.id))); val vm=SoloPartyViewModel(state); assertEquals(listOf(public.id),vm.selectedIds.value) }
    @Test fun `selected public ids survive viewmodel recreation`() { val state=SavedStateHandle(); val a=SoloPartyViewModel(state); PersonaCatalog.soloPublic.take(3).forEach{a.toggle(it.id)}; assertEquals(a.selectedIds.value,SoloPartyViewModel(state).selectedIds.value) }
    @Test fun `solo player uses localized default and editable name survives recreation`() { val state=SavedStateHandle(); val first=SoloPartyViewModel(state); assertEquals(SoloPartyViewModel.DEFAULT_PLAYER_NAME, first.playerName.value); first.setPlayerName("Alex"); assertEquals("Alex", SoloPartyViewModel(state).playerName.value) }
    @Test fun `blank solo player name normalizes back to localized default`() { val vm=SoloPartyViewModel(SavedStateHandle()); vm.setPlayerName("   "); assertEquals(SoloPartyViewModel.DEFAULT_PLAYER_NAME, vm.playerName.value) }
    @Test fun `solo mode defaults to who of us and survives recreation`() {
        val state = SavedStateHandle()
        val first = SoloPartyViewModel(state)
        assertEquals(GameMode.WHO_OF_US, first.selectedMode.value)
        first.setMode(GameMode.RED_GREEN)
        assertEquals(GameMode.RED_GREEN, SoloPartyViewModel(state).selectedMode.value)
    }

    @Test fun `solo mode rejects unsupported knows me`() {
        val vm = SoloPartyViewModel(SavedStateHandle())
        vm.setMode(GameMode.KNOWS_ME)
        assertEquals(GameMode.WHO_OF_US, vm.selectedMode.value)
        assertEquals(
            listOf(GameMode.WHO_OF_US, GameMode.MOST_LIKELY, GameMode.RED_GREEN),
            SoloPartyViewModel.SOLO_MODES,
        )
    }
}

