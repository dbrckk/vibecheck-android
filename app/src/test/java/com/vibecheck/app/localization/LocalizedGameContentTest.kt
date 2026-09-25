package com.vibecheck.app.localization

import com.vibecheck.app.data.QuestionRepository
import com.vibecheck.app.domain.model.GameMode
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalizedGameContentTest {
    @Test
    fun `French locale keeps French game content`() {
        withLocale(Locale.FRENCH) {
            assertEquals("Qui de nous ?", GameMode.WHO_OF_US.title)
            val question = QuestionRepository.forMode(GameMode.WHO_OF_US, seed = 1L, limit = 24)
                .first { it.id == "who_01" }
            assertEquals("Qui survivrait le mieux à une apocalypse ?", question.text)
        }
    }

    @Test
    fun `non French locale uses English game content`() {
        withLocale(Locale.GERMAN) {
            assertEquals("Who of us?", GameMode.WHO_OF_US.title)
            val question = QuestionRepository.forMode(GameMode.WHO_OF_US, seed = 1L, limit = 24)
                .first { it.id == "who_01" }
            assertEquals("Who would survive an apocalypse best?", question.text)
            assertTrue(question.text.none { it == 'é' || it == 'à' })
        }
    }

    private inline fun withLocale(locale: Locale, block: () -> Unit) {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(locale)
            block()
        } finally {
            Locale.setDefault(previous)
        }
    }
}
