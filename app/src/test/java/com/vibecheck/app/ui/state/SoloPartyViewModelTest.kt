package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.solo.PersonaKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloPartyViewModelTest {
    @Test
    fun `selection requires at least two companions before start`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())
        val first = PersonaCatalog.all.first()

        viewModel.toggle(first.id)

        assertFalse(viewModel.canStart.value)
        assertEquals(1, viewModel.selectedIds.value.size)
        assertTrue(viewModel.validationMessage.value.contains("2"))
    }

    @Test
    fun `selection accepts two through seven companions`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())
        PersonaCatalog.all.take(7).forEach { viewModel.toggle(it.id) }

        assertTrue(viewModel.canStart.value)
        assertEquals(7, viewModel.selectedIds.value.size)
        assertEquals("", viewModel.validationMessage.value)
    }

    @Test
    fun `eighth companion is rejected without losing current selection`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())
        PersonaCatalog.all.take(8).forEach { viewModel.toggle(it.id) }

        assertEquals(7, viewModel.selectedIds.value.size)
        assertTrue(viewModel.validationMessage.value.contains("7"))
    }

    @Test
    fun `tapping selected companion deselects it`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())
        val persona = PersonaCatalog.all.first()

        viewModel.toggle(persona.id)
        viewModel.toggle(persona.id)

        assertTrue(viewModel.selectedIds.value.isEmpty())
    }

    @Test
    fun `filter switches between public simulations and originals`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())

        viewModel.setKindFilter(PersonaKind.ORIGINAL)

        assertTrue(viewModel.visiblePersonas.value.isNotEmpty())
        assertTrue(viewModel.visiblePersonas.value.all { it.kind == PersonaKind.ORIGINAL })
    }

    @Test
    fun `search matches persona display name case insensitively`() {
        val viewModel = SoloPartyViewModel(SavedStateHandle())

        viewModel.setQuery("zend")

        assertEquals(listOf("Zendaya"), viewModel.visiblePersonas.value.map { it.displayName })
    }

    @Test
    fun `auto compose selects a valid mixed party reproducibly`() {
        val first = SoloPartyViewModel(SavedStateHandle())
        val second = SoloPartyViewModel(SavedStateHandle())

        first.autoCompose(seed = 42L, count = 5)
        second.autoCompose(seed = 42L, count = 5)

        assertEquals(first.selectedIds.value, second.selectedIds.value)
        assertEquals(5, first.selectedIds.value.size)
        assertTrue(first.canStart.value)
        val selected = PersonaCatalog.all.filter { it.id in first.selectedIds.value }
        assertTrue(selected.any { it.kind == PersonaKind.PUBLIC_SIMULATION })
        assertTrue(selected.any { it.kind == PersonaKind.ORIGINAL })
    }

    @Test
    fun `selected companion ids survive viewmodel recreation`() {
        val state = SavedStateHandle()
        val first = SoloPartyViewModel(state)
        PersonaCatalog.all.take(3).forEach { first.toggle(it.id) }

        val restored = SoloPartyViewModel(state)

        assertEquals(first.selectedIds.value, restored.selectedIds.value)
        assertTrue(restored.canStart.value)
    }
}
