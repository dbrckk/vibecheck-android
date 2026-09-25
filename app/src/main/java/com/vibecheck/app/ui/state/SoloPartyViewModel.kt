package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.localization.AppLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class SoloPartyViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val selectedIds: StateFlow<List<String>> = savedStateHandle.getStateFlow(KEY_SELECTED_IDS, emptyList())
    val playerName: StateFlow<String> = savedStateHandle.getStateFlow(KEY_PLAYER_NAME, DEFAULT_PLAYER_NAME)
    private val _query = MutableStateFlow(""); val query: StateFlow<String> = _query
    private val _kindFilter = MutableStateFlow<PersonaKind?>(null); val kindFilter: StateFlow<PersonaKind?> = _kindFilter
    private val _visiblePersonas = MutableStateFlow(PersonaCatalog.all); val visiblePersonas: StateFlow<List<Persona>> = _visiblePersonas
    private val _canStart = MutableStateFlow(selectedIds.value.size in MIN_COMPANIONS..MAX_COMPANIONS); val canStart: StateFlow<Boolean> = _canStart
    private val _validationMessage = MutableStateFlow(validationFor(selectedIds.value.size)); val validationMessage: StateFlow<String> = _validationMessage

    fun setPlayerName(value: String) { savedStateHandle[KEY_PLAYER_NAME] = value.trim().take(24).ifBlank { DEFAULT_PLAYER_NAME } }
    fun toggle(personaId: String) { if (PersonaCatalog.all.none{it.id==personaId}) return; val c=selectedIds.value; val u=when { personaId in c -> c.filterNot{it==personaId}; c.size>=MAX_COMPANIONS -> { updateValidation(c.size,true); return }; else -> c+personaId }; savedStateHandle[KEY_SELECTED_IDS]=u; updateValidation(u.size) }
    fun setKindFilter(kind: PersonaKind?) { _kindFilter.value=kind; refreshVisiblePersonas() }
    fun setQuery(value: String) { _query.value=value; refreshVisiblePersonas() }
    fun autoCompose(seed: Long, count: Int) { require(count in MIN_COMPANIONS..MAX_COMPANIONS); val public=PersonaCatalog.all.filter{it.kind==PersonaKind.PUBLIC_SIMULATION}.shuffled(Random(seed)); val originals=PersonaCatalog.all.filter{it.kind==PersonaKind.ORIGINAL}.shuffled(Random(seed xor 0x5DEECE66DL)); val selected=buildList { add(public.first()); add(originals.first()); addAll((public.drop(1)+originals.drop(1)).shuffled(Random(seed xor 0xC6A4A7935BD1E995UL.toLong())).take(count-size)) }.map{it.id}; savedStateHandle[KEY_SELECTED_IDS]=selected; updateValidation(selected.size) }
    private fun refreshVisiblePersonas() { val q=_query.value.trim().lowercase(); val k=_kindFilter.value; _visiblePersonas.value=PersonaCatalog.all.filter{(k==null||it.kind==k)&&(q.isBlank()||it.displayName.lowercase().contains(q)||it.archetype.lowercase().contains(q))} }
    private fun updateValidation(size:Int,maxReached:Boolean=false){_canStart.value=size in MIN_COMPANIONS..MAX_COMPANIONS;_validationMessage.value=if(maxReached)AppLocale.pick("Maximum $MAX_COMPANIONS compagnons.","Maximum $MAX_COMPANIONS companions.") else validationFor(size)}
    private fun validationFor(size:Int)=when{size<MIN_COMPANIONS->AppLocale.pick("Sélectionne au moins $MIN_COMPANIONS compagnons.","Select at least $MIN_COMPANIONS companions.");size>MAX_COMPANIONS->AppLocale.pick("Maximum $MAX_COMPANIONS compagnons.","Maximum $MAX_COMPANIONS companions.");else->""}
    companion object { const val MIN_COMPANIONS=2; const val MAX_COMPANIONS=7; val DEFAULT_PLAYER_NAME:String get()=AppLocale.pick("Moi","Me"); private const val KEY_SELECTED_IDS="solo_party_selected_ids"; private const val KEY_PLAYER_NAME="solo_party_player_name" }
}
