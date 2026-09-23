package com.vibecheck.app.data

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
        Question("know_12", GameMode.KNOWS_ME, "Qui connaît le mieux les projets secrets ou futurs des autres ?")
    )

    fun forMode(mode: GameMode, seed: Long = 0L, limit: Int = 8): List<Question> =
        questions
            .filter { it.mode == mode }
            .shuffled(Random(seed))
            .take(limit.coerceAtLeast(0))
}
