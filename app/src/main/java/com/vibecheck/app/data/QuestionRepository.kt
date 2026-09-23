package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Question
import kotlin.random.Random

object QuestionRepository {
    private val questions = listOf(
        Question("who_01", GameMode.WHO_OF_US, "Qui survivrait le mieux à une apocalypse ?"),
        Question("who_02", GameMode.WHO_OF_US, "Qui répondrait à un message à 3 h du matin ?"),
        Question("who_03", GameMode.WHO_OF_US, "Qui pourrait devenir célèbre par accident ?"),
        Question("who_04", GameMode.WHO_OF_US, "Qui garderait le mieux un secret ?"),
        Question("who_05", GameMode.WHO_OF_US, "Qui partirait vivre dans un autre pays sans prévenir ?"),
        Question("who_06", GameMode.WHO_OF_US, "Qui ferait le meilleur espion ?"),
        Question("who_07", GameMode.WHO_OF_US, "Qui finirait par adopter cinq animaux ?"),
        Question("who_08", GameMode.WHO_OF_US, "Qui serait capable de gagner un concours télé ?"),
        Question("who_09", GameMode.WHO_OF_US, "Qui oublierait le plus facilement où il a posé son téléphone ?"),
        Question("who_10", GameMode.WHO_OF_US, "Qui serait le meilleur pour organiser une soirée improvisée ?"),
        Question("who_11", GameMode.WHO_OF_US, "Qui pourrait disparaître du groupe pendant trois jours sans répondre ?"),
        Question("who_12", GameMode.WHO_OF_US, "Qui rirait au pire moment possible ?"),
        Question("who_13", GameMode.WHO_OF_US, "Qui pourrait improviser un week-end entier sans rien réserver ?"),
        Question("who_14", GameMode.WHO_OF_US, "Qui serait le plus crédible dans une émission de télé-réalité ?"),
        Question("who_15", GameMode.WHO_OF_US, "Qui survivrait le plus longtemps sans réseaux sociaux ?"),
        Question("who_16", GameMode.WHO_OF_US, "Qui lancerait un projet fou et réussirait à embarquer tout le monde ?"),
        Question("who_17", GameMode.WHO_OF_US, "Qui ferait le meilleur colocataire ?"),
        Question("who_18", GameMode.WHO_OF_US, "Qui reconnaîtrait une chanson en moins de trois secondes ?"),
        Question("who_19", GameMode.WHO_OF_US, "Qui serait le plus difficile à battre à un jeu de bluff ?"),
        Question("who_20", GameMode.WHO_OF_US, "Qui pourrait changer complètement de style du jour au lendemain ?"),
        Question("who_21", GameMode.WHO_OF_US, "Qui aurait le plus de chances de devenir viral sans le vouloir ?"),
        Question("who_22", GameMode.WHO_OF_US, "Qui ferait le meilleur discours improvisé ?"),
        Question("who_23", GameMode.WHO_OF_US, "Qui réussirait à transformer une galère en bon souvenir ?"),
        Question("who_24", GameMode.WHO_OF_US, "Qui pourrait vivre une semaine avec seulement un sac à dos ?"),

        Question("likely_01", GameMode.MOST_LIKELY, "Qui est le plus susceptible de partir en voyage sur un coup de tête ?"),
        Question("likely_02", GameMode.MOST_LIKELY, "Qui est le plus susceptible de lancer son propre business ?"),
        Question("likely_03", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'oublier son propre anniversaire ?"),
        Question("likely_04", GameMode.MOST_LIKELY, "Qui est le plus susceptible de gagner un débat absurde ?"),
        Question("likely_05", GameMode.MOST_LIKELY, "Qui est le plus susceptible de supprimer les réseaux sociaux pendant un mois ?"),
        Question("likely_06", GameMode.MOST_LIKELY, "Qui est le plus susceptible de commander à manger à minuit ?"),
        Question("likely_07", GameMode.MOST_LIKELY, "Qui est le plus susceptible de déménager pour une nouvelle aventure ?"),
        Question("likely_08", GameMode.MOST_LIKELY, "Qui est le plus susceptible de devenir accro à un nouveau hobby ?"),
        Question("likely_09", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'arriver en retard avec une excellente excuse ?"),
        Question("likely_10", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'envoyer un vocal de cinq minutes ?"),
        Question("likely_11", GameMode.MOST_LIKELY, "Qui est le plus susceptible de se faire un ami dans une file d'attente ?"),
        Question("likely_12", GameMode.MOST_LIKELY, "Qui est le plus susceptible de tenter un défi complètement inutile ?"),
        Question("likely_13", GameMode.MOST_LIKELY, "Qui est le plus susceptible de réserver un billet d'avion le jour même ?"),
        Question("likely_14", GameMode.MOST_LIKELY, "Qui est le plus susceptible de lancer une chaîne ou un podcast ?"),
        Question("likely_15", GameMode.MOST_LIKELY, "Qui est le plus susceptible de répondre avec un mème plutôt qu'une phrase ?"),
        Question("likely_16", GameMode.MOST_LIKELY, "Qui est le plus susceptible de se retrouver dans une aventure improbable ?"),
        Question("likely_17", GameMode.MOST_LIKELY, "Qui est le plus susceptible de changer de métier par passion ?"),
        Question("likely_18", GameMode.MOST_LIKELY, "Qui est le plus susceptible de faire rire tout le monde pendant un moment sérieux ?"),
        Question("likely_19", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'acheter quelque chose juste parce que c'est en édition limitée ?"),
        Question("likely_20", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'organiser une surprise mémorable ?"),
        Question("likely_21", GameMode.MOST_LIKELY, "Qui est le plus susceptible de tester une tendance avant tout le monde ?"),
        Question("likely_22", GameMode.MOST_LIKELY, "Qui est le plus susceptible de vivre dans trois pays différents ?"),
        Question("likely_23", GameMode.MOST_LIKELY, "Qui est le plus susceptible de gagner à un jeu grâce au bluff ?"),
        Question("likely_24", GameMode.MOST_LIKELY, "Qui est le plus susceptible de transformer un hobby en vrai projet ?"),

        Question("rg_01", GameMode.RED_GREEN, "Lire un message et répondre trois jours plus tard."),
        Question("rg_02", GameMode.RED_GREEN, "Toujours proposer de partager l'addition."),
        Question("rg_03", GameMode.RED_GREEN, "Annuler régulièrement au dernier moment."),
        Question("rg_04", GameMode.RED_GREEN, "Admettre rapidement quand on a tort."),
        Question("rg_05", GameMode.RED_GREEN, "Garder contact avec tous ses ex."),
        Question("rg_06", GameMode.RED_GREEN, "Ne jamais prêter son téléphone."),
        Question("rg_07", GameMode.RED_GREEN, "Prévenir quand on va être en retard."),
        Question("rg_08", GameMode.RED_GREEN, "Faire des compliments sans raison particulière."),
        Question("rg_09", GameMode.RED_GREEN, "Regarder les notifications de l'autre par-dessus son épaule."),
        Question("rg_10", GameMode.RED_GREEN, "Toujours vouloir choisir le programme de la soirée."),
        Question("rg_11", GameMode.RED_GREEN, "S'excuser clairement après une dispute."),
        Question("rg_12", GameMode.RED_GREEN, "Partager spontanément les tâches sans qu'on le demande."),
        Question("rg_13", GameMode.RED_GREEN, "Mettre son téléphone face cachée pendant toute une conversation."),
        Question("rg_14", GameMode.RED_GREEN, "Dire franchement qu'on a besoin de temps seul."),
        Question("rg_15", GameMode.RED_GREEN, "Liker régulièrement les anciennes photos d'une personne."),
        Question("rg_16", GameMode.RED_GREEN, "Proposer de parler calmement après un désaccord."),
        Question("rg_17", GameMode.RED_GREEN, "Vouloir connaître tous les mots de passe de l'autre."),
        Question("rg_18", GameMode.RED_GREEN, "Respecter un non sans demander d'explication."),
        Question("rg_19", GameMode.RED_GREEN, "Comparer souvent la relation à celles vues sur les réseaux sociaux."),
        Question("rg_20", GameMode.RED_GREEN, "Encourager les projets personnels même quand ils prennent du temps."),
        Question("rg_21", GameMode.RED_GREEN, "Faire des blagues sur un sujet que l'autre a demandé d'éviter."),
        Question("rg_22", GameMode.RED_GREEN, "Pouvoir passer une soirée chacun de son côté sans problème."),
        Question("rg_23", GameMode.RED_GREEN, "Parler d'un conflit à tout le groupe avant d'en parler à la personne concernée."),
        Question("rg_24", GameMode.RED_GREEN, "Dire clairement ce qu'on attend au lieu de faire deviner."),

        Question("know_01", GameMode.KNOWS_ME, "Qui connaît le mieux les habitudes du groupe ?"),
        Question("know_02", GameMode.KNOWS_ME, "Qui devinerait le mieux la destination de vacances idéale des autres ?"),
        Question("know_03", GameMode.KNOWS_ME, "Qui remarque le plus vite quand quelqu'un ne va pas bien ?"),
        Question("know_04", GameMode.KNOWS_ME, "Qui se souvient le mieux des petites anecdotes ?"),
        Question("know_05", GameMode.KNOWS_ME, "Qui devinerait le plat préféré de chacun ?"),
        Question("know_06", GameMode.KNOWS_ME, "Qui saurait quelle chanson met quelqu'un de bonne humeur ?"),
        Question("know_07", GameMode.KNOWS_ME, "Qui connaît le mieux les petites manies des autres ?"),
        Question("know_08", GameMode.KNOWS_ME, "Qui devinerait le plus facilement le cadeau parfait ?"),
        Question("know_09", GameMode.KNOWS_ME, "Qui sait le mieux ce qui énerve chaque personne du groupe ?"),
        Question("know_10", GameMode.KNOWS_ME, "Qui se rappelle le mieux des anciennes discussions du groupe ?"),
        Question("know_11", GameMode.KNOWS_ME, "Qui reconnaîtrait le plus vite une fausse anecdote racontée par un ami ?"),
        Question("know_12", GameMode.KNOWS_ME, "Qui connaît le mieux les projets secrets ou futurs des autres ?"),
        Question("know_13", GameMode.KNOWS_ME, "Qui saurait quelle personne appeler pour remonter le moral de chacun ?"),
        Question("know_14", GameMode.KNOWS_ME, "Qui devinerait le mieux la commande habituelle de tout le monde ?"),
        Question("know_15", GameMode.KNOWS_ME, "Qui saurait quelle situation met chacun le plus mal à l'aise ?"),
        Question("know_16", GameMode.KNOWS_ME, "Qui se souvient le mieux des dates importantes du groupe ?"),
        Question("know_17", GameMode.KNOWS_ME, "Qui devinerait le mieux le prochain gros achat de chacun ?"),
        Question("know_18", GameMode.KNOWS_ME, "Qui saurait quel film ou quelle série recommander à chacun ?"),
        Question("know_19", GameMode.KNOWS_ME, "Qui connaît le mieux les expressions que chacun répète tout le temps ?"),
        Question("know_20", GameMode.KNOWS_ME, "Qui devinerait le mieux ce que chacun ferait avec un mois de vacances ?"),
        Question("know_21", GameMode.KNOWS_ME, "Qui saurait immédiatement quand quelqu'un fait semblant d'aller bien ?"),
        Question("know_22", GameMode.KNOWS_ME, "Qui se rappelle le mieux des premières impressions de chacun ?"),
        Question("know_23", GameMode.KNOWS_ME, "Qui devinerait le mieux le rêve un peu secret de chaque personne ?"),
        Question("know_24", GameMode.KNOWS_ME, "Qui connaît le mieux les habitudes que chacun essaie de changer ?")
    )

    fun forMode(
        mode: GameMode,
        seed: Long = 0L,
        limit: Int = 8,
        avoidIds: Set<String> = emptySet(),
        intensity: GameIntensity? = null
    ): List<Question> {
        val shuffled = questions
            .filter { it.mode == mode }
            .filter { intensity == null || matchesIntensity(it.id, intensity) }
            .shuffled(Random(seed))
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
}
