package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.ui.theme.VibeCheckTheme
import com.vibecheck.app.ui.theme.VibeThemeStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun appearance_settings_show_all_six_themes_and_select_immediately() {
        var selected = VibeThemeStyle.PREMIUM_DARK

        composeRule.setContent {
            VibeCheckTheme(style = selected) {
                SettingsScreen(
                    selectedTheme = selected,
                    onThemeSelected = { selected = it },
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("Apparence").assertIsDisplayed()
        VibeThemeStyle.entries.forEach { style ->
            composeRule.onNodeWithText(style.label).assertIsDisplayed()
        }
        composeRule.onNodeWithText("Aucun compte requis", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Kawaii").performClick()
        composeRule.runOnIdle { assertEquals(VibeThemeStyle.KAWAII, selected) }
    }

    @Test
    fun back_action_is_exposed() {
        var backed = false
        composeRule.setContent {
            VibeCheckTheme {
                SettingsScreen(
                    selectedTheme = VibeThemeStyle.PREMIUM_DARK,
                    onThemeSelected = {},
                    onBack = { backed = true }
                )
            }
        }

        composeRule.onNodeWithText("Retour").performClick()
        composeRule.runOnIdle { assertTrue(backed) }
    }
}
