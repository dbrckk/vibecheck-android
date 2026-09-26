package com.vibecheck.app.data

import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaGender
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.domain.solo.PersonaTraits

object PersonaCatalog {
    private fun public(name: String, gender: PersonaGender, archetype: String, description: String, traits: PersonaTraits) =
        Persona("public_${name.lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_')}", name, gender, PersonaKind.PUBLIC_SIMULATION, archetype, description, traits)

    private fun original(id: String, name: String, gender: PersonaGender, archetype: String, description: String, traits: PersonaTraits) =
        Persona("original_$id", name, gender, PersonaKind.ORIGINAL, archetype, description, traits)

    val all: List<Persona> = listOf(
        public("Taylor Swift", PersonaGender.WOMAN, "La conteuse", "Persona ludique réfléchie, expressive et attentive aux dynamiques sociales.", t(72,55,82,65,68,82,35,78)),
        public("Beyonce", PersonaGender.WOMAN, "La performeuse", "Persona ludique assurée, disciplinée et tournée vers l'excellence.", t(58,82,72,68,88,55,25,82)),
        public("Rihanna", PersonaGender.WOMAN, "L'audacieuse", "Persona ludique directe, indépendante et joueuse.", t(78,90,62,76,74,48,67,55)),
        public("Ariana Grande", PersonaGender.WOMAN, "La sensible pop", "Persona ludique expressive, chaleureuse et spontanée.", t(75,62,82,78,60,76,58,52)),
        public("Zendaya", PersonaGender.WOMAN, "La posée", "Persona ludique calme, observatrice et élégante.", t(65,62,82,60,64,55,25,86)),
        public("Lady Gaga", PersonaGender.WOMAN, "L'excentrique", "Persona ludique créative, théâtrale et très ouverte.", t(82,91,80,80,70,72,82,58)),
        public("Dua Lipa", PersonaGender.WOMAN, "La cool", "Persona ludique sociable, confiante et détendue.", t(70,76,68,82,65,58,52,60)),
        public("Billie Eilish", PersonaGender.WOMAN, "L'ironique", "Persona ludique introspective, décalée et créative.", t(86,68,72,48,52,45,72,70)),
        public("Selena Gomez", PersonaGender.WOMAN, "La bienveillante", "Persona ludique empathique, douce et sociale.", t(66,48,92,72,50,70,30,72)),
        public("Miley Cyrus", PersonaGender.WOMAN, "La libre", "Persona ludique franche, énergique et imprévisible.", t(82,92,65,82,72,55,80,45)),
        public("Emma Watson", PersonaGender.WOMAN, "La réfléchie", "Persona ludique mesurée, curieuse et analytique.", t(55,55,82,50,60,48,18,94)),
        public("Margot Robbie", PersonaGender.WOMAN, "La joueuse", "Persona ludique vive, sociable et compétitive.", t(82,76,70,84,78,55,62,58)),
        public("Adele", PersonaGender.WOMAN, "La franche", "Persona ludique chaleureuse, drôle et sans détour.", t(90,72,84,78,55,72,48,60)),
        public("Shakira", PersonaGender.WOMAN, "L'énergique", "Persona ludique expressive, déterminée et sociable.", t(74,78,70,82,80,62,55,62)),
        public("Jennifer Lawrence", PersonaGender.WOMAN, "La spontanée", "Persona ludique humoristique, directe et imprévisible.", t(94,82,72,88,68,48,82,42)),

        public("Dwayne Johnson", PersonaGender.MAN, "Le motivateur", "Persona ludique positive, sociable et très compétitive.", t(78,82,76,94,90,48,35,62)),
        public("Ryan Reynolds", PersonaGender.MAN, "Le sarcastique", "Persona ludique rapide, taquine et très humoristique.", t(98,76,68,88,62,55,76,55)),
        public("Keanu Reeves", PersonaGender.MAN, "Le calme", "Persona ludique réservée, bienveillante et réfléchie.", t(58,42,94,45,48,52,12,92)),
        public("Chris Hemsworth", PersonaGender.MAN, "L'aventurier", "Persona ludique énergique, sportive et bon public.", t(80,82,70,88,82,50,62,52)),
        public("Tom Holland", PersonaGender.MAN, "L'enthousiaste", "Persona ludique vive, amicale et spontanée.", t(84,65,82,90,60,62,65,48)),
        public("Harry Styles", PersonaGender.MAN, "Le charmeur", "Persona ludique ouverte, créative et chaleureuse.", t(78,72,82,86,48,82,58,58)),
        public("Ed Sheeran", PersonaGender.MAN, "Le sentimental", "Persona ludique modeste, drôle et romantique.", t(80,48,86,72,45,92,35,68)),
        public("The Weeknd", PersonaGender.MAN, "Le nocturne", "Persona ludique mystérieuse, intense et introspective.", t(55,72,58,42,62,78,58,78)),
        public("Bruno Mars", PersonaGender.MAN, "Le showman", "Persona ludique festive, charmeuse et rythmée.", t(88,82,72,94,72,82,65,48)),
        public("Snoop Dogg", PersonaGender.MAN, "Le détendu", "Persona ludique calme, drôle et très sociale.", t(92,65,70,90,48,40,62,42)),
        public("Jack Black", PersonaGender.MAN, "Le chaos comique", "Persona ludique théâtrale, explosive et très drôle.", t(100,88,72,95,62,42,94,28)),
        public("Lewis Hamilton", PersonaGender.MAN, "Le compétiteur", "Persona ludique précise, ambitieuse et maîtrisée.", t(52,78,70,62,98,42,22,92)),
        public("Kylian Mbappe", PersonaGender.MAN, "Le challenger", "Persona ludique rapide, ambitieuse et orientée performance.", t(58,82,62,68,96,38,38,78)),
        public("Omar Sy", PersonaGender.MAN, "Le chaleureux", "Persona ludique accessible, expressive et humoristique.", t(90,68,88,94,58,52,52,55)),
        public("Pedro Pascal", PersonaGender.MAN, "Le complice", "Persona ludique chaleureuse, taquine et expressive.", t(90,72,88,88,55,62,62,55)),

        original("nova", "Nova", PersonaGender.WOMAN, "L'impulsive", "Elle répond vite, adore provoquer les retournements et assume ses choix.", t(82,94,55,84,76,62,96,22)),
        original("iris", "Iris", PersonaGender.WOMAN, "L'analyste", "Elle observe longtemps avant de choisir et repère les contradictions.", t(52,48,78,45,72,42,18,98)),
        original("mika", "Mika", PersonaGender.NON_BINARY, "Le wildcard", "Imprévisible mais attachant, iel transforme souvent une réponse simple en surprise.", t(92,82,72,86,55,58,98,32)),
        original("luna", "Luna", PersonaGender.WOMAN, "La romantique", "Elle privilégie les liens, l'intuition et les réponses émotionnelles.", t(68,52,94,72,42,100,35,58)),
        original("jade", "Jade", PersonaGender.WOMAN, "La stratège", "Compétitive et méthodique, elle cherche le choix le plus avantageux.", t(48,72,62,58,100,38,22,96)),
        original("max", "Max", PersonaGender.MAN, "Le boute-en-train", "Il choisit souvent la réponse qui fera le plus rire le groupe.", t(100,84,65,96,52,42,92,25)),
        original("noah", "Noah", PersonaGender.MAN, "Le diplomate", "Il cherche l'équilibre et évite de blesser les autres participants.", t(62,38,100,82,35,68,15,88)),
        original("axel", "Axel", PersonaGender.MAN, "Le rival", "Il transforme presque tout en compétition et adore prendre des risques.", t(72,96,52,78,100,35,82,45)),
        original("eli", "Eli", PersonaGender.NON_BINARY, "L'artiste", "Iel préfère les choix originaux, sensibles et inattendus.", t(84,68,88,72,38,82,78,62)),
        original("sam", "Sam", PersonaGender.MAN, "Le zen", "Il reste calme, privilégie les choix raisonnés et désamorce le chaos.", t(58,35,92,64,42,55,8,100))
    )

    private fun t(h: Int, b: Int, e: Int, s: Int, c: Int, r: Int, chaos: Int, d: Int) =
        PersonaTraits(h, b, e, s, c, r, chaos, d)
}
