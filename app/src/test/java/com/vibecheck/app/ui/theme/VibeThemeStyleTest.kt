package com.vibecheck.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class VibeThemeStyleTest {
    @Test
    fun catalog_has_six_stable_unique_styles() {
        val styles = VibeThemeStyle.entries
        assertEquals(6, styles.size)
        assertEquals(6, styles.map { it.id }.distinct().size)
        assertEquals(
            setOf("premium_dark", "kawaii", "pop", "elegant", "street", "minimal"),
            styles.map { it.id }.toSet()
        )
    }

    @Test
    fun unknown_or_missing_value_falls_back_to_premium_dark() {
        assertEquals(VibeThemeStyle.PREMIUM_DARK, VibeThemeStyle.fromId(null))
        assertEquals(VibeThemeStyle.PREMIUM_DARK, VibeThemeStyle.fromId(""))
        assertEquals(VibeThemeStyle.PREMIUM_DARK, VibeThemeStyle.fromId("future-theme"))
    }

    @Test
    fun known_value_round_trips() {
        VibeThemeStyle.entries.forEach { style ->
            assertEquals(style, VibeThemeStyle.fromId(style.id))
        }
    }

    @Test
    fun labels_are_user_facing_and_distinct() {
        VibeThemeStyle.entries.forEach { style ->
            assertNotEquals(style.id, style.label)
        }
        assertEquals(6, VibeThemeStyle.entries.map { it.label }.distinct().size)
    }
}
