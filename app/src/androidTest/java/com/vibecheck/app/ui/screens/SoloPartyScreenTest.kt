package com.vibecheck.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vibecheck.app.data.PersonaCatalog
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.ui.theme.VibeCheckTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SoloPartyScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun screen_shows_human_participant_simulation_disclaimer_and_primary_controls() {
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.all.take(8),selectedIds=emptyList(),selectedKind=null,query="",playerName="Moi",
            validationMessage="Choisis au moins 2 compagnons.",canStart=false,onBack={},onQueryChange={},onPlayerNameChange={},onKindChange={},onTogglePersona={},onAutoCompose={},onStart={}
        ) } }
        composeRule.onNodeWithText("Ta soirée solo").assertIsDisplayed()
        composeRule.onNodeWithText("Participant humain").assertIsDisplayed()
        composeRule.onNodeWithText("Simulation fictive",substring=true).assertIsDisplayed()
        composeRule.onNodeWithText("Composer automatiquement").assertIsDisplayed()
        composeRule.onNodeWithText("Choisis au moins 2 compagnons.").assertIsDisplayed()
    }

    @Test fun screen_exposes_public_and_original_filters() {
        var selected:PersonaKind?=null
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.all.take(8),selectedIds=emptyList(),selectedKind=selected,query="",playerName="Moi",validationMessage="",canStart=false,
            onBack={},onQueryChange={},onPlayerNameChange={},onKindChange={selected=it},onTogglePersona={},onAutoCompose={},onStart={}
        ) } }
        composeRule.onNodeWithText("Public").performClick();composeRule.runOnIdle{assertEquals(PersonaKind.PUBLIC_SIMULATION,selected)}
        composeRule.onNodeWithText("Originaux").performClick();composeRule.runOnIdle{assertEquals(PersonaKind.ORIGINAL,selected)}
    }

    @Test fun valid_party_can_start_with_human_participant() {
        var started=false;val ids=PersonaCatalog.all.take(2).map{it.id}
        composeRule.setContent { VibeCheckTheme { SoloPartyScreen(
            personas=PersonaCatalog.all.take(8),selectedIds=ids,selectedKind=null,query="",playerName="Alex",validationMessage="",canStart=true,
            onBack={},onQueryChange={},onPlayerNameChange={},onKindChange={},onTogglePersona={},onAutoCompose={},onStart={started=true}
        ) } }
        composeRule.onNodeWithText("Je participe — démarrer").performClick();composeRule.runOnIdle{assertTrue(started)}
    }
}
