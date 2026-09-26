package com.vibecheck.app.domain.model

import com.vibecheck.app.localization.AppLocale

enum class GamePack(
    private val titleFr: String,
    private val titleEn: String,
    private val subtitleFr: String,
    private val subtitleEn: String
) {
    MIX("Mix", "Mix", "Un mélange équilibré", "A balanced mix"),
    FRIENDS("Friends", "Friends", "Pour les groupes d'amis", "For groups of friends"),
    DEEP("Deep", "Deep", "Plus personnel et révélateur", "More personal and revealing"),
    CHAOS("Chaos", "Chaos", "Le plus imprévisible", "The most unpredictable");

    val title: String get() = AppLocale.pick(titleFr, titleEn)
    val subtitle: String get() = AppLocale.pick(subtitleFr, subtitleEn)
}

enum class GameIntensity(
    private val titleFr: String,
    private val titleEn: String,
    private val subtitleFr: String,
    private val subtitleEn: String
) {
    CHILL("Chill", "Chill", "Léger et facile à lancer", "Light and easy to start"),
    NORMAL("Normal", "Normal", "Le bon équilibre", "The right balance"),
    SAVAGE("Savage", "Savage", "Plus piquant et révélateur", "Sharper and more revealing");

    val title: String get() = AppLocale.pick(titleFr, titleEn)
    val subtitle: String get() = AppLocale.pick(subtitleFr, subtitleEn)
}

enum class GameMode(
    private val titleFr: String,
    private val titleEn: String,
    private val subtitleFr: String,
    private val subtitleEn: String
) {
    WHO_OF_US("Qui de nous ?", "Who of us?", "Vote pour la personne qui correspond le mieux.", "Vote for the person who fits best."),
    MOST_LIKELY("Most Likely To", "Most Likely To", "Le plus probable de faire ça.", "Who is most likely to do it."),
    RED_GREEN("Red Flag / Green Flag", "Red Flag / Green Flag", "Décide si le comportement passe ou casse.", "Decide whether the behavior is a green flag or a red flag."),
    KNOWS_ME("Qui me connaît le mieux ?", "Who knows me best?", "Une personne répond en secret. Les autres doivent deviner.", "One person answers secretly. Everyone else has to guess.");

    val title: String get() = AppLocale.pick(titleFr, titleEn)
    val subtitle: String get() = AppLocale.pick(subtitleFr, subtitleEn)
}

data class Question(val id: String, val mode: GameMode, val text: String)
data class Vote(val questionId: String, val answer: String)
data class GameResult(val winner: String, val score: Int, val total: Int)
