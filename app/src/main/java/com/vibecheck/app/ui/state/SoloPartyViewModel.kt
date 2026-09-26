package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.localization.AppLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class SoloPartyViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val initialSelected = savedStateHandle.get<List<String>>(KEY_SELECTED_IDS).orEmpty().filter { id -> PersonaCatalog.soloPublic.any { it.id == id } }.distinct().take(MAX_COMPANIONS)
    private val initialMode = savedStateHandle.get<String>(KEY_MODE)
        ?.let { stored -> runCatching { GameMode.valueOf(stored) }.getOrNull() }
        ?.takeIf { it in SOLO_MODES }
        ?: GameMode.WHO_OF_US
    init {
        savedStateHandle[KEY_SELECTED_IDS] = initialSelected
        savedStateHandle[KEY_MODE] = initialMode.name
    }
    val selectedIds: StateFlow<List<String>> = savedStateHandle.getStateFlow(KEY_SELECTED_IDS, initialSelected)
    private val _selectedMode = MutableStateFlow(initialMode)
    val selectedMode: StateFlow<GameMode> = _selectedMode
    val playerName: StateFlow<String> = savedStateHandle.getStateFlow(KEY_PLAYER_NAME, DEFAULT_PLAYER_NAME)
    private val _query = MutableStateFlow(""); val query: StateFlow<String> = _query
    private val _kindFilter = MutableStateFlow<PersonaKind?>(null); val kindFilter: StateFlow<PersonaKind?> = _kindFilter
    private val _visiblePersonas = MutableStateFlow(PersonaCatalog.soloPublic); val visiblePersonas: StateFlow<List<Persona>> = _visiblePersonas
    private val _canStart = MutableStateFlow(initialSelected.size in MIN_COMPANIONS..MAX_COMPANIONS); val canStart: StateFlow<Boolean> = _canStart
    private val _validationMessage = MutableStateFlow(validationFor(initialSelected.size)); val validationMessage: StateFlow<String> = _validationMessage

    fun setPlayerName(value: String) { savedStateHandle[KEY_PLAYER_NAME] = value.trim().take(24).ifBlank { DEFAULT_PLAYER_NAME } }
    fun setMode(mode: GameMode) {
        if (mode !in SOLO_MODES) return
        _selectedMode.value = mode
        savedStateHandle[KEY_MODE] = mode.name
    }
    fun toggle(personaId: String) { if (PersonaCatalog.soloPublic.none{it.id==personaId}) return; val c=selectedIds.value; val u=when { personaId in c -> c.filterNot{it==personaId}; c.size>=MAX_COMPANIONS -> { updateValidation(c.size,true); return }; else -> c+personaId }; savedStateHandle[KEY_SELECTED_IDS]=u; updateValidation(u.size) }
    fun setKindFilter(kind: PersonaKind?) { _kindFilter.value=if(kind==PersonaKind.PUBLIC_SIMULATION) kind else null; refreshVisiblePersonas() }
    fun setQuery(value: String) { _query.value=value; refreshVisiblePersonas() }
    fun autoCompose(seed: Long, count: Int) { require(count in MIN_COMPANIONS..MAX_COMPANIONS); val selected=PersonaCatalog.soloPublic.shuffled(Random(seed)).take(count).map{it.id}; savedStateHandle[KEY_SELECTED_IDS]=selected; updateValidation(selected.size) }
    private fun refreshVisiblePersonas() { val q=_query.value.trim().lowercase(); _visiblePersonas.value=PersonaCatalog.soloPublic.filter{q.isBlank()||it.displayName.lowercase().contains(q)||it.archetype.lowercase().contains(q)} }
    private fun updateValidation(size:Int,maxReached:Boolean=false){_canStart.value=size in MIN_COMPANIONS..MAX_COMPANIONS;_validationMessage.value=if(maxReached)AppLocale.pick("Maximum $MAX_COMPANIONS personnalités.","Maximum $MAX_COMPANIONS public figures.") else validationFor(size)}
    private fun validationFor(size:Int)=when{size<MIN_COMPANIONS->AppLocale.pick("Sélectionne au moins $MIN_COMPANIONS personnalités.","Select at least $MIN_COMPANIONS public figures.");size>MAX_COMPANIONS->AppLocale.pick("Maximum $MAX_COMPANIONS personnalités.","Maximum $MAX_COMPANIONS public figures.");else->""}
    companion object {
        const val MIN_COMPANIONS = 2
        const val MAX_COMPANIONS = 7
        val SOLO_MODES = listOf(GameMode.WHO_OF_US, GameMode.MOST_LIKELY, GameMode.RED_GREEN)
        val DEFAULT_PLAYER_NAME: String get() = AppLocale.pick("Moi", "Me")
        private const val KEY_SELECTED_IDS = "solo_party_selected_ids"
        private const val KEY_PLAYER_NAME = "solo_party_player_name"
        private const val KEY_MODE = "solo_party_mode"
    }
}
