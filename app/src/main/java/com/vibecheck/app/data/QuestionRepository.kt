package com.vibecheck.app.data

import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.domain.model.Question
import com.vibecheck.app.localization.AppLocale
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


    private val englishById = mapOf(
        "who_01" to "Who would survive an apocalypse best?",
        "who_02" to "Who would reply to a message at 3 a.m.?",
        "who_03" to "Who could become famous by accident?",
        "who_04" to "Who would keep a secret best?",
        "who_05" to "Who would move to another country without warning anyone?",
        "who_06" to "Who would make the best spy?",
        "who_07" to "Who would end up adopting five pets?",
        "who_08" to "Who could win a TV competition?",
        "who_09" to "Who is most likely to forget where they put their phone?",
        "who_10" to "Who would be best at organizing a last-minute party?",
        "who_11" to "Who could disappear from the group chat for three days without replying?",
        "who_12" to "Who would laugh at the worst possible moment?",
        "who_13" to "Who could improvise an entire weekend without booking anything?",
        "who_14" to "Who would be most believable on a reality TV show?",
        "who_15" to "Who would last longest without social media?",
        "who_16" to "Who would start a wild project and get everyone on board?",
        "who_17" to "Who would make the best roommate?",
        "who_18" to "Who could recognize a song in under three seconds?",
        "who_19" to "Who would be hardest to beat in a bluffing game?",
        "who_20" to "Who could completely change their style overnight?",
        "who_21" to "Who is most likely to go viral without trying?",
        "who_22" to "Who would give the best improvised speech?",
        "who_23" to "Who could turn a disaster into a great memory?",
        "who_24" to "Who could live for a week with only a backpack?",

        "likely_01" to "Who is most likely to take a spontaneous trip?",
        "likely_02" to "Who is most likely to start their own business?",
        "likely_03" to "Who is most likely to forget their own birthday?",
        "likely_04" to "Who is most likely to win a ridiculous debate?",
        "likely_05" to "Who is most likely to delete social media for a month?",
        "likely_06" to "Who is most likely to order food at midnight?",
        "likely_07" to "Who is most likely to move away for a new adventure?",
        "likely_08" to "Who is most likely to become obsessed with a new hobby?",
        "likely_09" to "Who is most likely to arrive late with a brilliant excuse?",
        "likely_10" to "Who is most likely to send a five-minute voice note?",
        "likely_11" to "Who is most likely to make a friend while waiting in line?",
        "likely_12" to "Who is most likely to attempt a completely pointless challenge?",
        "likely_13" to "Who is most likely to book a flight the same day?",
        "likely_14" to "Who is most likely to start a channel or podcast?",
        "likely_15" to "Who is most likely to reply with a meme instead of a sentence?",
        "likely_16" to "Who is most likely to end up in an unbelievable adventure?",
        "likely_17" to "Who is most likely to change careers for something they love?",
        "likely_18" to "Who is most likely to make everyone laugh during a serious moment?",
        "likely_19" to "Who is most likely to buy something just because it is limited edition?",
        "likely_20" to "Who is most likely to organize an unforgettable surprise?",
        "likely_21" to "Who is most likely to try a trend before everyone else?",
        "likely_22" to "Who is most likely to live in three different countries?",
        "likely_23" to "Who is most likely to win a game by bluffing?",
        "likely_24" to "Who is most likely to turn a hobby into a real project?",

        "rg_01" to "Reading a message and replying three days later.",
        "rg_02" to "Always offering to split the bill.",
        "rg_03" to "Regularly cancelling at the last minute.",
        "rg_04" to "Quickly admitting when they are wrong.",
        "rg_05" to "Staying in touch with all their exes.",
        "rg_06" to "Never lending anyone their phone.",
        "rg_07" to "Letting people know when they are going to be late.",
        "rg_08" to "Giving compliments for no particular reason.",
        "rg_09" to "Looking at someone else's notifications over their shoulder.",
        "rg_10" to "Always wanting to choose what everyone does that evening.",
        "rg_11" to "Apologizing clearly after an argument.",
        "rg_12" to "Sharing chores without being asked.",
        "rg_13" to "Keeping their phone face down for an entire conversation.",
        "rg_14" to "Clearly saying they need some time alone.",
        "rg_15" to "Regularly liking someone's old photos.",
        "rg_16" to "Suggesting a calm conversation after a disagreement.",
        "rg_17" to "Wanting to know all of the other person's passwords.",
        "rg_18" to "Respecting a no without asking for an explanation.",
        "rg_19" to "Often comparing the relationship to ones seen on social media.",
        "rg_20" to "Supporting personal projects even when they take a lot of time.",
        "rg_21" to "Joking about something the other person asked them to avoid.",
        "rg_22" to "Being fine spending an evening separately.",
        "rg_23" to "Talking about a conflict with the whole group before speaking to the person involved.",
        "rg_24" to "Clearly saying what they expect instead of making the other person guess.",

        "know_01" to "Who knows the group's habits best?",
        "know_02" to "Who could best guess everyone else's ideal vacation destination?",
        "know_03" to "Who notices fastest when someone is not doing well?",
        "know_04" to "Who remembers the little stories best?",
        "know_05" to "Who could guess everyone's favorite food?",
        "know_06" to "Who would know which song cheers someone up?",
        "know_07" to "Who knows everyone else's little habits best?",
        "know_08" to "Who could most easily guess the perfect gift?",
        "know_09" to "Who best knows what annoys each person in the group?",
        "know_10" to "Who remembers old group conversations best?",
        "know_11" to "Who would spot a fake story told by a friend fastest?",
        "know_12" to "Who knows everyone else's secret or future plans best?",
        "know_13" to "Who would know exactly who to call to cheer each person up?",
        "know_14" to "Who could best guess everyone's usual order?",
        "know_15" to "Who would know which situation makes each person most uncomfortable?",
        "know_16" to "Who remembers the group's important dates best?",
        "know_17" to "Who could best guess everyone's next big purchase?",
        "know_18" to "Who would know which movie or series to recommend to each person?",
        "know_19" to "Who best knows the phrases everyone keeps repeating?",
        "know_20" to "Who could best guess what everyone would do with a month off?",
        "know_21" to "Who would immediately know when someone is pretending to be okay?",
        "know_22" to "Who remembers everyone's first impressions best?",
        "know_23" to "Who could best guess each person's secret dream?",
        "know_24" to "Who best knows the habits everyone is trying to change?"
    )

    fun forMode(
        mode: GameMode,
        seed: Long = 0L,
        limit: Int = 8,
        avoidIds: Set<String> = emptySet(),
        intensity: GameIntensity? = null,
        pack: GamePack = GamePack.MIX
    ): List<Question> {
        val shuffled = questions
            .map { question ->
                question.copy(text = AppLocale.pick(question.text, englishById[question.id] ?: question.text))
            }
            .filter { it.mode == mode }
            .filter { intensity == null || matchesIntensity(it.id, intensity) }
            .filter { matchesPack(it.id, pack) }
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
