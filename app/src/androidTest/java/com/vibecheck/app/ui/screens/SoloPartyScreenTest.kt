package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.ui.state.SoloPartyState
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SoloPartyScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun solo_screen_exposes_catalog_filters_disclaimer_and_validates_start() {
        var state = SoloPartyState()
        composeRule.setContent {
            VibeCheckTheme {
                SoloPartyScreen(
                    personas = PersonaCatalog.all,
                    state = state,
                    onToggle = { state = state.toggle(it) },
                    onAutoCompose = { state = state.autoCompose(PersonaCatalog.all, 5, 42L) },
                    onBack = {},
                    onStart = {}
                )
            }
        }

        composeRule.onNodeWithText("Célébrités").assertIsDisplayed()
        composeRule.onNodeWithText("Originaux").assertIsDisplayed()
        composeRule.onNodeWithText("Composer automatiquement").assertIsDisplayed()
        composeRule.onNodeWithText("Simulation fictive", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Commencer").assertIsNotEnabled()
        composeRule.onNodeWithText("Composer automatiquement").performClick()
        composeRule.onNodeWithText("Commencer").assertIsEnabled()
        composeRule.runOnIdle { assertEquals(5, state.selectedIds.size) }
    }
}
