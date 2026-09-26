package com.vibecheck.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class VibeActionSurfaceTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun action_surface_exposes_button_click_behavior() {
        var clicked = false
        composeRule.setContent {
            VibeCheckTheme {
                VibeActionSurface(onClick = { clicked = true }) {
                    Text("Test action", Modifier.padding(16.dp))
                }
            }
        }

        composeRule.onNodeWithText("Test action")
            .assertHasClickAction()
            .performClick()

        composeRule.runOnIdle { assertTrue(clicked) }
    }
}
