package com.vibecheck.app.ui.state

import com.vibecheck.app.data.PersonaCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SoloPartyStateTest {
    @Test
    fun party_requires_two_and_caps_at_seven() {
        var state = SoloPartyState()
        val personas = PersonaCatalog.all.take(8)
        assertFalse(state.canStart)
        personas.forEach { state = state.toggle(it.id) }
        assertEquals(7, state.selectedIds.size)
        assertTrue(state.canStart)
    }

    @Test
    fun toggling_selected_persona_removes_it() {
        val id = PersonaCatalog.all.first().id
        val selected = SoloPartyState().toggle(id)
        assertTrue(id in selected.selectedIds)
        assertFalse(id in selected.toggle(id).selectedIds)
    }

    @Test
    fun auto_compose_returns_balanced_valid_party() {
        val state = SoloPartyState().autoCompose(PersonaCatalog.all, size = 5, seed = 42L)
        assertEquals(5, state.selectedIds.size)
        assertTrue(state.canStart)
        val chosen = PersonaCatalog.all.filter { it.id in state.selectedIds }
        assertTrue(chosen.map { it.kind }.distinct().size >= 2)
        assertTrue(chosen.map { it.gender }.distinct().size >= 2)
    }
}
