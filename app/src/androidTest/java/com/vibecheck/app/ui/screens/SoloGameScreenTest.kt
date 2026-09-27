package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SoloGameScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun solo_reveal_labels_public_responses_as_fictional_simulation() {
        var continued = false

        composeRule.setContent {
            VibeCheckTheme {
                GameScreen(
                    mode = GameMode.WHO_OF_US,
                    questionText = "Qui tenterait le choix le plus risqué ?",
                    progress = 1,
                    total = 8,
                    answers = emptyList(),
                    onAnswer = {},
                    onExit = {},
                    simulatedResponses = listOf(
                        SimulatedResponseUi(
                            personaName = "Zendaya",
                            option = "Nova",
                            isFictionalSimulation = true,
                        ),
                        SimulatedResponseUi(
                            personaName = "Nova",
                            option = "Nova",
                            isFictionalSimulation = false,
                        ),
                    ),
                    onContinueSimulation = { continued = true },
                )
            }
        }

        composeRule.onNodeWithText("Simulation fictive", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Zendaya → Nova").assertIsDisplayed()
        composeRule.onNodeWithText("Nova → Nova").assertIsDisplayed()
        composeRule.onNodeWithText("Continuer").performClick()
        composeRule.runOnIdle { assertTrue(continued) }
    }
}
