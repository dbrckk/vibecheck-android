package com.vibecheck.app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CoreFlowScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun onboarding_explains_core_loop_and_starts() {
        var started = false

        composeRule.setContent {
            VibeCheckTheme {
                OnboardingScreen(onStart = { started = true })
            }
        }

        composeRule.onNodeWithText("Ton groupe. Vos réponses. Le verdict.").assertIsDisplayed()
        composeRule.onNodeWithText("Pas de compte requis • fonctionne hors ligne").assertIsDisplayed()
        composeRule.onNodeWithText("Commencer").performClick()

        composeRule.runOnIdle { assertTrue(started) }
    }

    @Test
    fun player_setup_adds_two_players_then_enables_start() {
        val players = mutableStateListOf<String>()
        var started = false

        composeRule.setContent {
            VibeCheckTheme {
                PlayerSetupScreen(
                    mode = GameMode.WHO_OF_US,
                    players = players,
                    challengeTarget = null,
                    onBack = {},
                    onAddPlayer = { players.add(it) },
                    onRemovePlayer = { players.remove(it) },
                    onStart = { started = true }
                )
            }
        }

        fun add(name: String) {
            composeRule.onNodeWithText("Prénom ou pseudo").performTextInput(name)
            composeRule.onNodeWithText("Ajouter le joueur").performClick()
        }

        add("Alex")
        add("Sam")

        composeRule.onNodeWithText("Alex").assertIsDisplayed()
        composeRule.onNodeWithText("Sam").assertIsDisplayed()
        composeRule.onNodeWithText("Lancer la partie").assertIsEnabled().performClick()

        composeRule.runOnIdle {
            assertEquals(listOf("Alex", "Sam"), players.toList())
            assertTrue(started)
        }
    }
}
