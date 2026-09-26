package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SoloPartyScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun screen_shows_human_participant_public_simulation_disclaimer_and_primary_controls() {
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.soloPublic.take(8),selectedIds=emptyList(),selectedMode=GameMode.WHO_OF_US,query="",playerName="Moi",
            validationMessage="Sélectionne au moins 2 personnalités.",canStart=false,onBack={},onQueryChange={},onPlayerNameChange={},onModeChange={},onTogglePersona={},onAutoCompose={},onStart={}
        ) } }
        composeRule.onNodeWithText("Ta soirée solo").assertIsDisplayed()
        composeRule.onNodeWithText("TOI • PARTICIPANT RÉEL").assertIsDisplayed()
        composeRule.onNodeWithText("SIMULATION FICTIVE").assertIsDisplayed()
        composeRule.onNodeWithText("Composer automatiquement").assertIsDisplayed()
        composeRule.onNodeWithText(GameMode.WHO_OF_US.title).assertIsDisplayed()
        composeRule.onNodeWithText(GameMode.MOST_LIKELY.title).assertIsDisplayed()
        composeRule.onNodeWithText("Sélectionne au moins 2 personnalités.").assertIsDisplayed()
    }

    @Test fun screen_never_exposes_original_persona_filter() {
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.soloPublic.take(8),selectedIds=emptyList(),selectedMode=GameMode.WHO_OF_US,query="",playerName="Moi",validationMessage="",canStart=false,
            onBack={},onQueryChange={},onPlayerNameChange={},onModeChange={},onTogglePersona={},onAutoCompose={},onStart={}
        ) } }
        composeRule.onAllNodesWithText("Originaux").assertCountEquals(0)
        composeRule.onAllNodesWithText("Originals").assertCountEquals(0)
    }

    @Test fun valid_party_can_start_with_human_participant() {
        var started=false;val ids=PersonaCatalog.soloPublic.take(2).map{it.id}
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.soloPublic.take(8),selectedIds=ids,selectedMode=GameMode.WHO_OF_US,query="",playerName="Alex",validationMessage="",canStart=true,
            onBack={},onQueryChange={},onPlayerNameChange={},onModeChange={},onTogglePersona={},onAutoCompose={},onStart={started=true}
        ) } }
        composeRule.onNodeWithText("Je participe — démarrer").performClick();composeRule.runOnIdle{assertTrue(started)}
    }
}
