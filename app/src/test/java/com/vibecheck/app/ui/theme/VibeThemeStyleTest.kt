package com.vibecheck.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VibeThemeStyleTest {
    @Test
    fun `catalog exposes six stable unique theme ids`() {
        val styles = VibeThemeStyle.entries
        assertEquals(6, styles.size)
        assertEquals(6, styles.map { it.id }.toSet().size)
        assertEquals(
            listOf("premium_dark", "kawaii", "pop", "elegant", "street", "minimal"),
            styles.map { it.id }
        )
    }

    @Test
    fun `stored id parser restores known style`() {
        assertEquals(VibeThemeStyle.KAWAII, VibeThemeStyle.fromStoredId("kawaii"))
        assertEquals(VibeThemeStyle.MINIMAL, VibeThemeStyle.fromStoredId("minimal"))
    }

    @Test
    fun `unknown corrupt or missing stored id falls back safely`() {
        listOf(null, "", "unknown", "KAWAII", " premium_dark ").forEach { raw ->
            assertEquals(VibeThemeStyle.PREMIUM_DARK, VibeThemeStyle.fromStoredId(raw))
        }
    }

    @Test
    fun `every style has a non blank display label`() {
        assertTrue(VibeThemeStyle.entries.all { it.label.isNotBlank() })
    }
}
