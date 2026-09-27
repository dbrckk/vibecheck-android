package com.vibecheck.app.domain.solo

enum class PersonaKind {
    PUBLIC_SIMULATION,
    ORIGINAL,
}

enum class PersonaPresentation {
    MASCULINE,
    FEMININE,
    NEUTRAL,
}

data class Persona(
    val id: String,
    val displayName: String,
    val kind: PersonaKind,
    val presentation: PersonaPresentation,
    val archetype: String,
    val traits: PersonaTraits,
) {
    val isFictionalSimulation: Boolean
        get() = kind == PersonaKind.PUBLIC_SIMULATION
}
