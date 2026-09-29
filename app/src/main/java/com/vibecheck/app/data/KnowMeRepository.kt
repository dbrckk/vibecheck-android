package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.localization.AppLocale
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


    private val englishById = mapOf(
        "km_01" to ("For a perfect weekend?" to listOf("Beach", "Mountains")),
        "km_02" to ("For an ideal evening?" to listOf("Go out", "Stay home")),
        "km_03" to ("When waking up?" to listOf("Coffee", "20 more minutes")),
        "km_04" to ("When traveling?" to listOf("Plan everything", "Improvise")),
        "km_05" to ("To relax?" to listOf("Music", "Series or movie")),
        "km_06" to ("In a group?" to listOf("Talk a lot", "Observe first")),
        "km_07" to ("For a gift?" to listOf("An experience", "An object")),
        "km_08" to ("On Sunday?" to listOf("Stay active", "Plan nothing")),
        "km_09" to ("On vacation?" to listOf("Explore", "Rest")),
        "km_10" to ("To communicate?" to listOf("Messages", "Calls")),
        "km_11" to ("When making a choice?" to listOf("Instinct", "Think it through")),
        "km_12" to ("For food?" to listOf("Sweet", "Savory")),
        "km_13" to ("For a destination?" to listOf("Big city", "Nature")),
        "km_14" to ("For an activity?" to listOf("Competition", "Cooperation")),
        "km_15" to ("When something is wrong?" to listOf("Talk about it quickly", "Take a step back")),
        "km_16" to ("To discover something new?" to listOf("Read / research", "Try it directly")),
        "km_17" to ("For photos?" to listOf("Take lots", "Enjoy it without the phone")),
        "km_18" to ("For a surprise?" to listOf("Love it", "Prefer to know")),
        "km_19" to ("To work?" to listOf("With noise", "In quiet")),
        "km_20" to ("For a fun purchase?" to listOf("Travel / outing", "Tech / object")),
        "km_21" to ("Facing the unexpected?" to listOf("Adapt quickly", "Regain control")),
        "km_22" to ("For a new passion?" to listOf("All in immediately", "Little by little")),
        "km_23" to ("To celebrate good news?" to listOf("With a crowd", "With a small group")),
        "km_24" to ("For an important decision?" to listOf("Ask for opinions", "Decide alone"))
    )

    fun forSeed(
        seed: Long,
        limit: Int = 8,
        avoidIds: Set<String> = emptySet(),
        intensity: GameIntensity? = null,
        pack: GamePack = GamePack.MIX
    ): List<KnowMePrompt> {
        val filtered = prompts
            .map { prompt ->
                val en = englishById[prompt.id]
                if (en == null) prompt else prompt.copy(
                    text = AppLocale.pick(prompt.text, en.first),
                    options = prompt.options.mapIndexed { index, option ->
                        AppLocale.pick(option, en.second.getOrElse(index) { option })
                    }
                )
            }
            .filter { intensity == null || matchesIntensity(it.id, intensity) }
            .filter { matchesPack(it.id, pack) }
        val shuffled = filtered.shuffled(Random(seed))
        val preferred = shuffled.filterNot { it.id in avoidIds }
        val fallback = shuffled.filter { it.id in avoidIds }
        return (preferred + fallback).take(limit.coerceAtLeast(0))
    }
    private fun matchesIntensity(id: String, intensity: GameIntensity): Boolean {
        val index = id.substringAfterLast("_").toIntOrNull() ?: return intensity == GameIntensity.NORMAL
        return when (intensity) {
            GameIntensity.CHILL -> index in 1..16
            GameIntensity.NORMAL -> index in 5..20
            GameIntensity.SAVAGE -> index in 9..24
        }
    }
    private fun matchesPack(id: String, pack: GamePack): Boolean {
        if (pack == GamePack.MIX) return true
        val index = id.substringAfterLast("_").toIntOrNull() ?: return false
        val allowed = when (pack) {
            GamePack.MIX -> return true
            GamePack.FRIENDS -> setOf(1,2,3,4,6,7,8,10,11,12,13,16,17,18,20,23)
            GamePack.DEEP -> setOf(4,5,7,9,11,12,14,15,16,18,19,20,21,22,23,24)
            GamePack.CHAOS -> setOf(1,3,5,6,8,9,10,11,12,13,14,15,17,19,21,24)
        }
        return index in allowed
    }
}
