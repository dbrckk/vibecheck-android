package com.vibecheck.app.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
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

    @Test
    fun player_setup_ime_adds_player_and_clears_input() {
        val players = mutableStateListOf<String>()

        composeRule.setContent {
            VibeCheckTheme {
                PlayerSetupScreen(
                    mode = GameMode.WHO_OF_US,
                    players = players,
                    challengeTarget = null,
                    onBack = {},
                    onAddPlayer = { players.add(it) },
                    onRemovePlayer = { players.remove(it) },
                    onStart = {}
                )
            }
        }

        composeRule.onNodeWithText("Prénom ou pseudo").performTextInput("  Alex  ")
        composeRule.onNodeWithText("Prénom ou pseudo").performImeAction()

        composeRule.runOnIdle { assertEquals(listOf("Alex"), players.toList()) }
        composeRule.onNodeWithText("0/18").assertIsDisplayed()
    }

    @Test
    fun player_setup_remove_disables_start_when_group_drops_below_minimum() {
        val players = mutableStateListOf("Alex", "Sam")

        composeRule.setContent {
            VibeCheckTheme {
                PlayerSetupScreen(
                    mode = GameMode.WHO_OF_US,
                    players = players,
                    challengeTarget = null,
                    onBack = {},
                    onAddPlayer = { players.add(it) },
                    onRemovePlayer = { players.remove(it) },
                    onStart = {}
                )
            }
        }

        composeRule.onNodeWithText("Lancer la partie").assertIsEnabled()
        composeRule.onNodeWithContentDescription("Supprimer Sam").performClick()

        composeRule.onNodeWithText("Sam").assertDoesNotExist()
        composeRule.onNodeWithText("Ajoute au moins 2 joueurs").assertIsNotEnabled()
    }

    @Test
    fun player_setup_at_capacity_rejects_ninth_player() {
        val players = mutableStateListOf<String>().apply {
            addAll((1..8).map { "Joueur $it" })
        }

        composeRule.setContent {
            VibeCheckTheme {
                PlayerSetupScreen(
                    mode = GameMode.WHO_OF_US,
                    players = players,
                    challengeTarget = null,
                    onBack = {},
                    onAddPlayer = { players.add(it) },
                    onRemovePlayer = { players.remove(it) },
                    onStart = {}
                )
            }
        }

        composeRule.onNodeWithText("8/8").assertIsDisplayed()
        composeRule.onNodeWithText("Prénom ou pseudo").performTextInput("Joueur 9")
        composeRule.onNodeWithText("Ajouter le joueur").assertIsNotEnabled()
        composeRule.runOnIdle { assertEquals(8, players.size) }
    }

    @Test
    fun player_setup_back_invokes_navigation_callback() {
        var backed = false

        composeRule.setContent {
            VibeCheckTheme {
                PlayerSetupScreen(
                    mode = GameMode.WHO_OF_US,
                    players = emptyList(),
                    challengeTarget = null,
                    onBack = { backed = true },
                    onAddPlayer = {},
                    onRemovePlayer = {},
                    onStart = {}
                )
            }
        }

        composeRule.onNodeWithText("Retour").performClick()
        composeRule.runOnIdle { assertTrue(backed) }
    }
}
