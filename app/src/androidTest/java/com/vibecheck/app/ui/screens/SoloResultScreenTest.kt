package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.domain.model.GameResult
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Rule
import org.junit.Test

class SoloResultScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun solo_result_is_labelled_as_simulated_entertainment() {
        composeRule.setContent {
            VibeCheckTheme {
                ResultScreen(
                    mode = GameMode.WHO_OF_US,
                    votes = emptyList(),
                    resultOverride = GameResult(
                        winner = "Nova",
                        score = 4,
                        total = 8,
                    ),
                    challengeTarget = null,
                    sessionSeed = 42L,
                    sessionInstanceId = 9L,
                    intensity = GameIntensity.NORMAL,
                    pack = GamePack.MIX,
                    isSoloSession = true,
                    onReplay = {},
                    onHome = {},
                )
            }
        }

        composeRule
            .onNodeWithText("Résultat de simulation fictive", substring = true)
            .assertIsDisplayed()
    }
}
