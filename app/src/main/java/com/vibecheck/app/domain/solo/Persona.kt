package com.vibecheck.app.domain.solo

enum class PersonaKind { PUBLIC_SIMULATION, ORIGINAL }
enum class PersonaGender { MAN, WOMAN, NON_BINARY }

data class PersonaTraits(
    val humor: Int,
    val boldness: Int,
    val empathy: Int,
    val sociability: Int,
    val competitiveness: Int,
    val romanticism: Int,
    val chaos: Int,
    val deliberation: Int
) {
    fun values(): List<Int> = listOf(humor, boldness, empathy, sociability, competitiveness, romanticism, chaos, deliberation)
}

data class Persona(
    val id: String,
    val displayName: String,
    val gender: PersonaGender,
    val kind: PersonaKind,
    val archetype: String,
    val description: String,
    val traits: PersonaTraits
) {
    val isFictionalSimulation: Boolean get() = kind == PersonaKind.PUBLIC_SIMULATION
}
