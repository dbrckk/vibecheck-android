package com.vibecheck.app.domain.solo

data class PersonaTraits(
    val humor: Int,
    val boldness: Int,
    val empathy: Int,
    val sociability: Int,
    val competitiveness: Int,
    val romanticism: Int,
    val chaos: Int,
    val deliberation: Int,
) {
    init {
        require(values().all { it in MIN..MAX }) {
            "Persona traits must stay between $MIN and $MAX"
        }
    }

    fun values(): List<Int> = listOf(
        humor,
        boldness,
        empathy,
        sociability,
        competitiveness,
        romanticism,
        chaos,
        deliberation,
    )

    companion object {
        const val MIN = 0
        const val MAX = 100
    }
}
