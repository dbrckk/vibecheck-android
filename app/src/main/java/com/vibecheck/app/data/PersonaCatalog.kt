package com.vibecheck.app.data

import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.domain.solo.PersonaPresentation
import com.vibecheck.app.domain.solo.PersonaTraits

object PersonaCatalog {
    val all: List<Persona> = listOf(
        public("beyonce", "Beyoncé", PersonaPresentation.FEMININE, "Présence assurée, élégante et rassembleuse", 55, 82, 78, 72, 68, 64, 24, 78),
        public("taylor_swift", "Taylor Swift", PersonaPresentation.FEMININE, "Narrative, observatrice et très structurée", 58, 66, 76, 62, 72, 82, 26, 88),
        public("rihanna", "Rihanna", PersonaPresentation.FEMININE, "Cool, directe et très confiante", 72, 90, 58, 76, 76, 48, 54, 62),
        public("adele", "Adele", PersonaPresentation.FEMININE, "Chaleureuse, expressive et spontanément drôle", 84, 68, 86, 74, 48, 88, 42, 64),
        public("lady_gaga", "Lady Gaga", PersonaPresentation.FEMININE, "Théâtrale, créative et audacieuse", 68, 94, 80, 78, 66, 74, 72, 58),
        public("zendaya", "Zendaya", PersonaPresentation.FEMININE, "Posée, moderne et précise", 58, 72, 76, 64, 66, 52, 22, 86),
        public("dua_lipa", "Dua Lipa", PersonaPresentation.FEMININE, "Énergique, sociable et déterminée", 62, 78, 66, 82, 74, 58, 38, 70),
        public("ariana_grande", "Ariana Grande", PersonaPresentation.FEMININE, "Vive, sensible et joueuse", 72, 70, 82, 78, 52, 80, 46, 62),
        public("billie_eilish", "Billie Eilish", PersonaPresentation.FEMININE, "Décalée, introspective et imprévisible", 70, 76, 70, 48, 44, 58, 78, 68),
        public("shakira", "Shakira", PersonaPresentation.FEMININE, "Dynamique, chaleureuse et compétitive", 66, 82, 76, 84, 78, 64, 44, 70),
        public("selena_gomez", "Selena Gomez", PersonaPresentation.FEMININE, "Douce, calme et attentive", 56, 58, 90, 66, 42, 72, 22, 78),
        public("miley_cyrus", "Miley Cyrus", PersonaPresentation.FEMININE, "Franche, libre et imprévisible", 76, 92, 62, 82, 66, 54, 84, 50),
        public("jennifer_lopez", "Jennifer Lopez", PersonaPresentation.FEMININE, "Ambitieuse, solaire et disciplinée", 58, 84, 70, 82, 86, 66, 28, 82),
        public("emma_watson", "Emma Watson", PersonaPresentation.FEMININE, "Réfléchie, réservée et méthodique", 48, 64, 82, 48, 54, 58, 16, 94),
        public("margot_robbie", "Margot Robbie", PersonaPresentation.FEMININE, "Joueuse, assurée et adaptable", 76, 80, 68, 78, 64, 54, 52, 68),

        public("dwayne_johnson", "Dwayne Johnson", PersonaPresentation.MASCULINE, "Énergique, positif et ultra compétitif", 76, 90, 72, 92, 92, 48, 36, 68),
        public("ryan_reynolds", "Ryan Reynolds", PersonaPresentation.MASCULINE, "Ironique, rapide et très sociable", 96, 78, 64, 86, 62, 54, 66, 60),
        public("keanu_reeves", "Keanu Reeves", PersonaPresentation.MASCULINE, "Calme, discret et bienveillant", 48, 54, 94, 42, 38, 54, 12, 90),
        public("chris_hemsworth", "Chris Hemsworth", PersonaPresentation.MASCULINE, "Décontracté, sportif et joueur", 78, 82, 68, 86, 78, 52, 46, 62),
        public("tom_holland", "Tom Holland", PersonaPresentation.MASCULINE, "Enthousiaste, spontané et accessible", 82, 68, 76, 88, 58, 68, 54, 58),
        public("leonardo_dicaprio", "Leonardo DiCaprio", PersonaPresentation.MASCULINE, "Réservé, intense et calculé", 44, 72, 62, 46, 74, 48, 20, 92),
        public("brad_pitt", "Brad Pitt", PersonaPresentation.MASCULINE, "Cool, posé et subtilement compétitif", 62, 70, 62, 64, 66, 52, 30, 78),
        public("will_smith", "Will Smith", PersonaPresentation.MASCULINE, "Expressif, charismatique et très social", 84, 82, 70, 94, 72, 62, 52, 64),
        public("robert_downey_jr", "Robert Downey Jr.", PersonaPresentation.MASCULINE, "Vif, sarcastique et confiant", 92, 88, 58, 78, 78, 44, 68, 72),
        public("michael_b_jordan", "Michael B. Jordan", PersonaPresentation.MASCULINE, "Focus, ambitieux et maîtrisé", 54, 82, 70, 68, 90, 58, 22, 88),
        public("pedro_pascal", "Pedro Pascal", PersonaPresentation.MASCULINE, "Chaleureux, drôle et complice", 88, 72, 86, 88, 48, 66, 50, 64),
        public("harry_styles", "Harry Styles", PersonaPresentation.MASCULINE, "Créatif, ouvert et détendu", 72, 76, 82, 78, 44, 78, 52, 62),
        public("ed_sheeran", "Ed Sheeran", PersonaPresentation.MASCULINE, "Simple, romantique et sociable", 68, 58, 80, 72, 46, 92, 30, 72),
        public("drake", "Drake", PersonaPresentation.MASCULINE, "Compétitif, sûr de lui et émotionnel", 58, 84, 58, 76, 88, 82, 48, 66),
        public("the_weeknd", "The Weeknd", PersonaPresentation.MASCULINE, "Mystérieux, intense et introspectif", 40, 76, 52, 42, 68, 86, 42, 86),

        original("nova", "Nova", PersonaPresentation.FEMININE, "Optimiste cosmique qui transforme tout en aventure", 78, 76, 82, 84, 58, 74, 62, 54),
        original("milo", "Milo", PersonaPresentation.MASCULINE, "Stratège calme qui réfléchit avant chaque choix", 52, 58, 74, 54, 72, 44, 18, 96),
        original("pixel", "Pixel", PersonaPresentation.NEUTRAL, "Agent du chaos numérique, drôle et imprévisible", 94, 82, 56, 78, 64, 38, 98, 30),
        original("luna", "Luna", PersonaPresentation.FEMININE, "Rêveuse intuitive, empathique et romantique", 62, 54, 96, 68, 34, 98, 36, 72),
        original("jax", "Jax", PersonaPresentation.MASCULINE, "Compétiteur extraverti qui choisit vite", 80, 94, 48, 94, 98, 42, 72, 36),
        original("sage", "Sage", PersonaPresentation.NEUTRAL, "Observateur zen, prudent et analytique", 42, 40, 88, 42, 38, 46, 8, 100),
        original("ruby", "Ruby", PersonaPresentation.FEMININE, "Leader flamboyante, directe et très sociale", 78, 96, 66, 96, 84, 58, 70, 52),
        original("echo", "Echo", PersonaPresentation.NEUTRAL, "Caméléon social qui s'adapte au groupe", 72, 62, 88, 94, 52, 64, 48, 70),
        original("atlas", "Atlas", PersonaPresentation.MASCULINE, "Protecteur fiable, méthodique et loyal", 48, 68, 92, 62, 70, 58, 12, 94),
        original("zora", "Zora", PersonaPresentation.FEMININE, "Créative audacieuse qui adore les choix inattendus", 86, 92, 70, 84, 62, 72, 90, 46),
    )

    private fun public(
        id: String,
        displayName: String,
        presentation: PersonaPresentation,
        archetype: String,
        humor: Int,
        boldness: Int,
        empathy: Int,
        sociability: Int,
        competitiveness: Int,
        romanticism: Int,
        chaos: Int,
        deliberation: Int,
    ) = Persona(
        id = id,
        displayName = displayName,
        kind = PersonaKind.PUBLIC_SIMULATION,
        presentation = presentation,
        archetype = archetype,
        traits = PersonaTraits(
            humor = humor,
            boldness = boldness,
            empathy = empathy,
            sociability = sociability,
            competitiveness = competitiveness,
            romanticism = romanticism,
            chaos = chaos,
            deliberation = deliberation,
        ),
    )

    private fun original(
        id: String,
        displayName: String,
        presentation: PersonaPresentation,
        archetype: String,
        humor: Int,
        boldness: Int,
        empathy: Int,
        sociability: Int,
        competitiveness: Int,
        romanticism: Int,
        chaos: Int,
        deliberation: Int,
    ) = Persona(
        id = id,
        displayName = displayName,
        kind = PersonaKind.ORIGINAL,
        presentation = presentation,
        archetype = archetype,
        traits = PersonaTraits(
            humor = humor,
            boldness = boldness,
            empathy = empathy,
            sociability = sociability,
            competitiveness = competitiveness,
            romanticism = romanticism,
            chaos = chaos,
            deliberation = deliberation,
        ),
    )
}
