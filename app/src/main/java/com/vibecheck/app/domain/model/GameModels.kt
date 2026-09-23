package com.vibecheck.app.domain.model

enum class GameIntensity(val title: String, val subtitle: String) {
    CHILL("Chill", "Léger et facile à lancer"),
    NORMAL("Normal", "Le bon équilibre"),
    SAVAGE("Savage", "Plus piquant et révélateur")
}

enum class GameMode(val title: String, val subtitle: String) {
    WHO_OF_US("Qui de nous ?", "Vote pour la personne qui correspond le mieux."),
    MOST_LIKELY("Most Likely To", "Le plus probable de faire ça."),
    RED_GREEN("Red Flag / Green Flag", "Décide si le comportement passe ou casse."),
    KNOWS_ME("Qui me connaît le mieux ?", "Une personne répond en secret. Les autres doivent deviner.")
}
data class Question(val id: String, val mode: GameMode, val text: String)
data class Vote(val questionId: String, val answer: String)
data class GameResult(val winner: String, val score: Int, val total: Int)
