package com.vibecheck.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class VibeThemeTokensTest {
    @Test
    fun every_style_has_complete_distinct_visual_tokens() {
        val tokens = VibeThemeStyle.entries.map(::themeTokens)

        assertEquals(6, tokens.size)
        assertEquals(6, tokens.map { it.background }.distinct().size)
        assertEquals(6, tokens.map { it.primary }.distinct().size)
        tokens.forEach { token ->
            assertNotEquals(token.background, token.surface)
            assertNotEquals(token.primary, token.onPrimary)
            assertEquals(3, token.backdrop.size)
        }
    }

    @Test
    fun premium_dark_preserves_existing_brand_direction() {
        val token = themeTokens(VibeThemeStyle.PREMIUM_DARK)
        assertEquals(0xFF0B0A0FL, token.background)
        assertEquals(0xFFC9A7FFL, token.primary)
    }
}
