package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.billing.PurchaseStatus
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomePrimaryActionsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun home_prioritizes_together_and_solo_then_reveals_group_options() {
        var soloRequested = false

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
                    onOpenSettings = {},
                    onPlaySolo = { soloRequested = true },
                    onBuyPremium = {},
                    onIntensity = {},
                    onPack = {},
                    onMode = {},
                )
            }
        }

        composeRule.onNodeWithText("Jouer ensemble").assertIsDisplayed()
        composeRule.onNodeWithText("Jouer en solo").assertIsDisplayed()
        composeRule.onNodeWithText("Jouer ensemble").performClick()
        composeRule.onNodeWithText("CHOISIS TON VIBE").assertIsDisplayed()

        composeRule.onNodeWithText("Jouer en solo").performClick()
        composeRule.runOnIdle { assertTrue(soloRequested) }
    }
}
