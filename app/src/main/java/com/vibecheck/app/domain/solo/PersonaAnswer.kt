package com.vibecheck.app.domain.solo

data class PersonaAnswer(
    val personaId: String,
    val option: String,
    val confidence: Int,
    val isFictionalSimulation: Boolean,
)
