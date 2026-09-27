package com.vibecheck.app.localization

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLocaleTest {
    @Test fun `French language uses French copy`() {
        assertEquals("Bonjour", AppLocale.pickForLanguage("fr", "Bonjour", "Hello"))
        assertEquals("Bonjour", AppLocale.pickForLanguage("FR", "Bonjour", "Hello"))
    }

    @Test fun `every non French language falls back to English`() {
        listOf("en", "de", "es", "it", "pt", "ja", "ar", "", null).forEach { language ->
            assertEquals("Hello", AppLocale.pickForLanguage(language, "Bonjour", "Hello"))
        }
    }
}
