package com.vibecheck.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VibeThemeStyleTest {
    @Test
    fun `catalog exposes seven stable unique theme ids with pastel first`() {
        val styles = VibeThemeStyle.entries
        assertEquals(7, styles.size)
        assertEquals(7, styles.map { it.id }.toSet().size)
        assertEquals(
            listOf("pastel_drawn", "premium_dark", "kawaii", "pop", "elegant", "street", "minimal"),
            styles.map { it.id }
        )
    }

    @Test
    fun `stored id parser restores known style`() {
        assertEquals(VibeThemeStyle.PASTEL_DRAWN, VibeThemeStyle.fromStoredId("pastel_drawn"))
        assertEquals(VibeThemeStyle.KAWAII, VibeThemeStyle.fromStoredId("kawaii"))
        assertEquals(VibeThemeStyle.MINIMAL, VibeThemeStyle.fromStoredId("minimal"))
    }

    @Test
    fun `unknown corrupt or missing stored id falls back to pastel drawn`() {
        listOf(null, "", "unknown", "KAWAII", " premium_dark ").forEach { raw ->
            assertEquals(VibeThemeStyle.PASTEL_DRAWN, VibeThemeStyle.fromStoredId(raw))
        }
    }

    @Test
    fun `every style has a non blank display label`() {
        assertTrue(VibeThemeStyle.entries.all { it.label.isNotBlank() })
    }
}
