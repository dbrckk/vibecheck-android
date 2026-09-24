package com.vibecheck.app.data

import com.vibecheck.app.domain.solo.PersonaGender
import com.vibecheck.app.domain.solo.PersonaKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonaCatalogTest {
    @Test
    fun catalog_contains_public_and_original_cast() {
        val all = PersonaCatalog.all
        val public = all.filter { it.kind == PersonaKind.PUBLIC_SIMULATION }
        val originals = all.filter { it.kind == PersonaKind.ORIGINAL }

        assertTrue(all.size >= 40)
        assertTrue(public.size >= 30)
        assertTrue(originals.size >= 10)
        assertTrue(public.count { it.gender == PersonaGender.MAN } >= 15)
        assertTrue(public.count { it.gender == PersonaGender.WOMAN } >= 15)
    }

    @Test
    fun persona_ids_are_unique_and_stable_safe_keys() {
        val ids = PersonaCatalog.all.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
        ids.forEach { id ->
            assertTrue(id.matches(Regex("[a-z0-9_]+")))
        }
    }

    @Test
    fun every_profile_has_bounded_traits_and_nonempty_copy() {
        PersonaCatalog.all.forEach { persona ->
            assertTrue(persona.displayName.isNotBlank())
            assertTrue(persona.archetype.isNotBlank())
            assertTrue(persona.description.isNotBlank())
            persona.traits.values().forEach { value -> assertTrue(value in 0..100) }
        }
    }

    @Test
    fun public_profiles_are_always_marked_as_fictional_simulations() {
        PersonaCatalog.all
            .filter { it.kind == PersonaKind.PUBLIC_SIMULATION }
            .forEach { persona -> assertTrue(persona.isFictionalSimulation) }
    }
}
