package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Question

object QuestionRepository {
    private val questions = listOf(
        Question("who_01", GameMode.WHO_OF_US, "Qui survivrait le mieux à une apocalypse ?"),
        Question("who_02", GameMode.WHO_OF_US, "Qui répondrait à un message à 3 h du matin ?"),
        Question("who_03", GameMode.WHO_OF_US, "Qui pourrait devenir célèbre par accident ?"),
        Question("who_04", GameMode.WHO_OF_US, "Qui garderait le mieux un secret ?"),
        Question("likely_01", GameMode.MOST_LIKELY, "Qui est le plus susceptible de partir en voyage sur un coup de tête ?"),
        Question("likely_02", GameMode.MOST_LIKELY, "Qui est le plus susceptible de lancer son propre business ?"),
        Question("likely_03", GameMode.MOST_LIKELY, "Qui est le plus susceptible d'oublier son propre anniversaire ?"),
        Question("likely_04", GameMode.MOST_LIKELY, "Qui est le plus susceptible de gagner un débat absurde ?"),
        Question("rg_01", GameMode.RED_GREEN, "Lire un message et répondre trois jours plus tard."),
        Question("rg_02", GameMode.RED_GREEN, "Toujours proposer de partager l'addition."),
        Question("rg_03", GameMode.RED_GREEN, "Annuler régulièrement au dernier moment."),
        Question("rg_04", GameMode.RED_GREEN, "Admettre rapidement quand on a tort."),
        Question("know_01", GameMode.KNOWS_ME, "Qui connaît le mieux les habitudes du groupe ?"),
        Question("know_02", GameMode.KNOWS_ME, "Qui devinerait le mieux la destination de vacances idéale des autres ?"),
        Question("know_03", GameMode.KNOWS_ME, "Qui remarque le plus vite quand quelqu'un ne va pas bien ?"),
        Question("know_04", GameMode.KNOWS_ME, "Qui se souvient le mieux des petites anecdotes ?")
    )
    fun forMode(mode: GameMode, limit: Int = 8): List<Question> =
        questions.filter { it.mode == mode }.take(limit)
}
