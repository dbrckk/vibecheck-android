package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity
import kotlin.random.Random

data class KnowMePrompt(
    val id: String,
    val text: String,
    val options: List<String>
)

object KnowMeRepository {
    private val prompts = listOf(
        KnowMePrompt("km_01", "Pour un week-end parfait ?", listOf("Mer", "Montagne")),
        KnowMePrompt("km_02", "Pour une soirée idéale ?", listOf("Sortir", "Rester chez soi")),
        KnowMePrompt("km_03", "Au réveil ?", listOf("Café", "Encore 20 minutes")),
        KnowMePrompt("km_04", "Pour voyager ?", listOf("Tout planifier", "Improviser")),
        KnowMePrompt("km_05", "Pour se détendre ?", listOf("Musique", "Série ou film")),
        KnowMePrompt("km_06", "Dans un groupe ?", listOf("Parler beaucoup", "Observer d'abord")),
        KnowMePrompt("km_07", "Pour un cadeau ?", listOf("Une expérience", "Un objet")),
        KnowMePrompt("km_08", "Le dimanche ?", listOf("Bouger", "Ne rien prévoir")),
        KnowMePrompt("km_09", "En vacances ?", listOf("Explorer", "Se reposer")),
        KnowMePrompt("km_10", "Pour communiquer ?", listOf("Messages", "Appels")),
        KnowMePrompt("km_11", "Quand il faut choisir ?", listOf("Instinct", "Réflexion")),
        KnowMePrompt("km_12", "Pour manger ?", listOf("Sucré", "Salé")),
        KnowMePrompt("km_13", "Pour une destination ?", listOf("Grande ville", "Nature")),
        KnowMePrompt("km_14", "Pour une activité ?", listOf("Compétition", "Coopération")),
        KnowMePrompt("km_15", "Quand quelque chose ne va pas ?", listOf("En parler vite", "Prendre du recul")),
        KnowMePrompt("km_16", "Pour découvrir quelque chose ?", listOf("Lire / chercher", "Tester directement")),
        KnowMePrompt("km_17", "Pour les photos ?", listOf("En prendre plein", "Profiter sans téléphone")),
        KnowMePrompt("km_18", "Pour une surprise ?", listOf("Adorer", "Préférer savoir")),
        KnowMePrompt("km_19", "Pour travailler ?", listOf("Avec du bruit", "Au calme")),
        KnowMePrompt("km_20", "Pour une dépense plaisir ?", listOf("Voyage / sortie", "Tech / objet")),
        KnowMePrompt("km_21", "Face à un imprévu ?", listOf("S'adapter vite", "Reprendre le contrôle")),
        KnowMePrompt("km_22", "Pour une nouvelle passion ?", listOf("À fond immédiatement", "Petit à petit")),
        KnowMePrompt("km_23", "Pour fêter une bonne nouvelle ?", listOf("Avec du monde", "En petit comité")),
        KnowMePrompt("km_24", "Pour prendre une décision importante ?", listOf("Demander des avis", "Décider seul"))
    )

    fun forSeed(
        seed: Long,
        limit: Int = 8,
        avoidIds: Set<String> = emptySet(),
        intensity: GameIntensity? = null
    ): List<KnowMePrompt> {
        val filtered = prompts.filter { intensity == null || intensityForId(it.id) == intensity }
        val shuffled = filtered.shuffled(Random(seed))
        val preferred = shuffled.filterNot { it.id in avoidIds }
        val fallback = shuffled.filter { it.id in avoidIds }
        return (preferred + fallback).take(limit.coerceAtLeast(0))
    }
    private fun intensityForId(id: String): GameIntensity {
        val index = id.substringAfterLast("_").toIntOrNull() ?: return GameIntensity.NORMAL
        return when (index) {
            in 1..8 -> GameIntensity.CHILL
            in 9..16 -> GameIntensity.NORMAL
            else -> GameIntensity.SAVAGE
        }
    }
}
