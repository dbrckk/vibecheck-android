package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Question
import com.vibecheck.app.domain.solo.SoloRoundResolver
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SoloGameScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun solo_round_reveals_simulated_votes_then_advances() {
        val cast = PersonaCatalog.all.take(3)
        val question = Question("who_test", GameMode.WHO_OF_US, "Qui improviserait le mieux ?")
        val round = SoloRoundResolver().resolve(question, cast, 42L)
        var advanced = false

        composeRule.setContent {
            VibeCheckTheme {
                SoloGameScreen(
                    questionText = question.text,
                    progress = 1,
                    total = 8,
                    cast = cast,
                    round = round,
                    onNext = { advanced = true },
                    onExit = {}
                )
            }
        }

        composeRule.onNodeWithText("Révéler les réponses").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Simulation fictive", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Question suivante").assertIsDisplayed().performClick()
        composeRule.runOnIdle { assertTrue(advanced) }
    }
}
