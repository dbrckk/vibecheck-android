package com.vibecheck.app.ui.state

import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaGender
import com.vibecheck.app.domain.solo.PersonaKind

data class SoloPartyState(
    val selectedIds: List<String> = emptyList()
) {
    val canStart: Boolean get() = selectedIds.size in MIN_PARTY_SIZE..MAX_PARTY_SIZE

    fun toggle(personaId: String): SoloPartyState {
        if (personaId in selectedIds) return copy(selectedIds = selectedIds - personaId)
        if (selectedIds.size >= MAX_PARTY_SIZE) return this
        return copy(selectedIds = selectedIds + personaId)
    }

    fun autoCompose(catalog: List<Persona>, size: Int = 5, seed: Long): SoloPartyState {
        val target = size.coerceIn(MIN_PARTY_SIZE, MAX_PARTY_SIZE)
        if (catalog.isEmpty()) return SoloPartyState()

        val ordered = catalog.distinctBy { it.id }.sortedBy { stableRank(it.id, seed) }
        val picked = mutableListOf<Persona>()

        fun addFirst(predicate: (Persona) -> Boolean) {
            ordered.firstOrNull { candidate -> candidate !in picked && predicate(candidate) }?.let(picked::add)
        }

        // Seed a varied cast before filling by deterministic rank.
        addFirst { it.kind == PersonaKind.PUBLIC_SIMULATION && it.gender == PersonaGender.WOMAN }
        addFirst { it.kind == PersonaKind.PUBLIC_SIMULATION && it.gender == PersonaGender.MAN }
        addFirst { it.kind == PersonaKind.ORIGINAL }

        ordered.forEach { if (picked.size < target && it !in picked) picked += it }
        return SoloPartyState(picked.take(target).map { it.id })
    }

    companion object {
        const val MIN_PARTY_SIZE = 2
        const val MAX_PARTY_SIZE = 7

        private fun stableRank(id: String, seed: Long): Long {
            var hash = seed xor -7046029254386353131L
            id.forEach { hash = (hash xor it.code.toLong()) * 1099511628211L }
            return hash xor (hash ushr 32)
        }
    }
}
