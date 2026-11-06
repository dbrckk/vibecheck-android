package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
                    selectedIntensity = GameIntensity.NORMAL,
                    selectedPack = GamePack.MIX,
                    savedPlayerCount = 0,
                    groupLeaderName = null,
                    groupLeaderWins = 0,
                    onOpenLeaderboard = {},
                    onEditGroup = {},
                    onOpenSettings = {},
                    onPlaySolo = { soloRequested = true },
                    onIntensity = {},
                    onPack = {},
                    onMode = {},
                )
            }
        }

        composeRule.onNodeWithText("Jouer ensemble").assertIsDisplayed().assertHasClickAction()
        composeRule.onNodeWithText("Jouer en solo").assertIsDisplayed().assertHasClickAction()
        composeRule.onNodeWithText("Jouer ensemble").performClick()
        composeRule.onNodeWithText("CHOISIS TON VIBE").assertIsDisplayed()

        composeRule.onNodeWithText("Jouer en solo").performClick()
        composeRule.runOnIdle { assertTrue(soloRequested) }
    }
    @Test
    fun home_exposes_leaderboard_when_group_has_a_recorded_leader() {
        var leaderboardRequested = false

        composeRule.setContent {
            VibeCheckTheme {
                HomeScreen(
                    selectedIntensity = GameIntensity.NORMAL,
                    selectedPack = GamePack.MIX,
                    savedPlayerCount = 3,
                    groupLeaderName = "Alex",
                    groupLeaderWins = 2,
                    onOpenLeaderboard = { leaderboardRequested = true },
                    onEditGroup = {},
                    onOpenSettings = {},
                    onPlaySolo = {},
                    onIntensity = {},
                    onPack = {},
                    onMode = {},
                )
            }
        }

        composeRule.onNodeWithText("Leader : Alex • 2 victoires").assertIsDisplayed()
        composeRule.onNodeWithText("Classement").assertIsDisplayed().performClick()
        composeRule.runOnIdle { assertTrue(leaderboardRequested) }
    }

}
