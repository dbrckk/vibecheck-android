package com.vibecheck.app.domain.solo

data class PersonaOption(
    val id: String,
    val label: String,
    val traits: PersonaTraits
)

data class PersonaAnswer(
    val personaId: String,
    val optionId: String?,
    val score: Int,
    val reaction: String,
    val isFictionalSimulation: Boolean
)
