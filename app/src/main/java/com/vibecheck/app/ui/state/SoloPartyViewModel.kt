package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class SoloPartyViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val selectedIds: StateFlow<List<String>> =
        savedStateHandle.getStateFlow(KEY_SELECTED_IDS, emptyList())

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _kindFilter = MutableStateFlow<PersonaKind?>(null)
    val kindFilter: StateFlow<PersonaKind?> = _kindFilter

    private val _visiblePersonas = MutableStateFlow(PersonaCatalog.all)
    val visiblePersonas: StateFlow<List<Persona>> = _visiblePersonas

    private val _canStart = MutableStateFlow(selectedIds.value.size in MIN_COMPANIONS..MAX_COMPANIONS)
    val canStart: StateFlow<Boolean> = _canStart

    private val _validationMessage = MutableStateFlow(validationFor(selectedIds.value.size))
    val validationMessage: StateFlow<String> = _validationMessage

    fun toggle(personaId: String) {
        if (PersonaCatalog.all.none { it.id == personaId }) return

        val current = selectedIds.value
        val updated = when {
            personaId in current -> current.filterNot { it == personaId }
            current.size >= MAX_COMPANIONS -> {
                updateValidation(current.size, maxReached = true)
                return
            }
            else -> current + personaId
        }

        savedStateHandle[KEY_SELECTED_IDS] = updated
        updateValidation(updated.size)
    }

    fun setKindFilter(kind: PersonaKind?) {
        _kindFilter.value = kind
        refreshVisiblePersonas()
    }

    fun setQuery(value: String) {
        _query.value = value
        refreshVisiblePersonas()
    }

    fun autoCompose(seed: Long, count: Int) {
        require(count in MIN_COMPANIONS..MAX_COMPANIONS) {
            "Solo party size must stay between $MIN_COMPANIONS and $MAX_COMPANIONS"
        }

        val random = Random(seed)
        val public = PersonaCatalog.all
            .filter { it.kind == PersonaKind.PUBLIC_SIMULATION }
            .shuffled(random)
        val originals = PersonaCatalog.all
            .filter { it.kind == PersonaKind.ORIGINAL }
            .shuffled(Random(seed xor 0x5DEECE66DL))

        val selected = buildList {
            add(public.first())
            add(originals.first())

            val remaining = (public.drop(1) + originals.drop(1))
                .shuffled(Random(seed xor 0xC6A4A7935BD1E995UL.toLong()))

            addAll(remaining.take(count - size))
        }.map { it.id }

        savedStateHandle[KEY_SELECTED_IDS] = selected
        updateValidation(selected.size)
    }

    private fun refreshVisiblePersonas() {
        val normalizedQuery = _query.value.trim().lowercase()
        val selectedKind = _kindFilter.value

        _visiblePersonas.value = PersonaCatalog.all.filter { persona ->
            val kindMatches = selectedKind == null || persona.kind == selectedKind
            val queryMatches = normalizedQuery.isBlank() ||
                persona.displayName.lowercase().contains(normalizedQuery) ||
                persona.archetype.lowercase().contains(normalizedQuery)

            kindMatches && queryMatches
        }
    }

    private fun updateValidation(size: Int, maxReached: Boolean = false) {
        _canStart.value = size in MIN_COMPANIONS..MAX_COMPANIONS
        _validationMessage.value = when {
            maxReached -> "Maximum $MAX_COMPANIONS compagnons."
            else -> validationFor(size)
        }
    }

    private fun validationFor(size: Int): String = when {
        size < MIN_COMPANIONS -> "Sélectionne au moins $MIN_COMPANIONS compagnons."
        size > MAX_COMPANIONS -> "Maximum $MAX_COMPANIONS compagnons."
        else -> ""
    }

    companion object {
        const val MIN_COMPANIONS = 2
        const val MAX_COMPANIONS = 7
        private const val KEY_SELECTED_IDS = "solo_party_selected_ids"
    }
}
