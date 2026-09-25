package com.vibecheck.app.data

import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.domain.solo.PersonaPresentation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonaCatalogTest {
    @Test
    fun `catalog ships enough public simulations and originals`() {
        val personas = PersonaCatalog.all
        val public = personas.filter { it.kind == PersonaKind.PUBLIC_SIMULATION }
        val originals = personas.filter { it.kind == PersonaKind.ORIGINAL }

        assertTrue(personas.size >= 40)
        assertTrue(public.size >= 30)
        assertTrue(originals.size >= 10)
        assertTrue(public.count { it.presentation == PersonaPresentation.MASCULINE } >= 15)
        assertTrue(public.count { it.presentation == PersonaPresentation.FEMININE } >= 15)
    }

    @Test
    fun `persona ids are stable and unique`() {
        val personas = PersonaCatalog.all
        assertEquals(personas.size, personas.map { it.id }.toSet().size)
        assertTrue(personas.all { it.id.isNotBlank() && it.displayName.isNotBlank() })
    }

    @Test
    fun `all persona traits stay inside the supported scale`() {
        PersonaCatalog.all.forEach { persona ->
            val values = persona.traits.values()
            assertEquals(8, values.size)
            assertTrue(values.all { it in 0..100 })
        }
    }

    @Test
    fun `public entries are always explicitly fictional simulations`() {
        val public = PersonaCatalog.all.filter { it.kind == PersonaKind.PUBLIC_SIMULATION }
        assertTrue(public.all { it.isFictionalSimulation })
        assertTrue(public.all { it.archetype.isNotBlank() })
    }
}
