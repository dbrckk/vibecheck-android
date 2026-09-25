package com.vibecheck.app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.billing.PurchaseStatus
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GamePack
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
    fun home_exposes_compact_settings_entry() {
        var opened = false

        composeRule.setContent {
            VibeCheckTheme {
                HomeScreen(
                    isPremium = false,
                    premiumReady = false,
                    premiumPrice = null,
                    purchaseStatus = PurchaseStatus.IDLE,
                    selectedIntensity = GameIntensity.NORMAL,
                    selectedPack = GamePack.MIX,
                    savedPlayerCount = 0,
                    groupLeaderName = null,
                    groupLeaderWins = 0,
                    onOpenLeaderboard = {},
                    onEditGroup = {},
                    onOpenSettings = { opened = true },
                    onBuyPremium = {},
                    onIntensity = {},
                    onPack = {},
                    onMode = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Ouvrir les réglages").performClick()
        composeRule.runOnIdle { assertTrue(opened) }
    }

    @Test
    fun settings_shows_six_themes_and_retains_kawaii_selection_on_return() {
        var selectedStyle by mutableStateOf(VibeThemeStyle.PREMIUM_DARK)
        var backed = false

        composeRule.setContent {
            VibeCheckTheme(style = selectedStyle) {
                SettingsScreen(
                    selectedStyle = selectedStyle,
                    onStyleSelected = { selectedStyle = it },
                    onBack = { backed = true },
                )
            }
        }

        composeRule.onNodeWithText("Apparence").assertIsDisplayed()
        VibeThemeStyle.entries.forEach { style ->
            composeRule.onNodeWithText(style.label).assertIsDisplayed()
        }

        composeRule.onNodeWithText("Kawaii").performClick()

        composeRule.runOnIdle {
            assertEquals(VibeThemeStyle.KAWAII, selectedStyle)
        }
        composeRule
            .onNodeWithContentDescription("Thème Kawaii sélectionné")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Retour").performClick()
        composeRule.runOnIdle {
            assertTrue(backed)
            assertEquals(VibeThemeStyle.KAWAII, selectedStyle)
        }
    }
}
