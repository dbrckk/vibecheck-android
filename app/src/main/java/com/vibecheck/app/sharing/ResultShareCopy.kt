package com.vibecheck.app.sharing

import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.localization.AppLocale

object ResultShareCopy {
    fun localized(
        mode: GameMode,
        winner: String,
        percent: Int,
        podium: String,
        fictionalSimulation: Boolean,
    ): String = AppLocale.pick(
        french(mode, winner, percent, podium, fictionalSimulation),
        english(mode, winner, percent, podium, fictionalSimulation),
    )

    fun english(
        mode: GameMode,
        winner: String,
        percent: Int,
        podium: String,
        fictionalSimulation: Boolean,
    ): String = when {
        fictionalSimulation ->
            "My fictional VibeCheck simulation: $winner comes out on top with $percent%. " +
                "Public-figure responses are simulated for entertainment and are not their real opinions."
        mode == GameMode.KNOWS_ME ->
            "My VibeCheck: $winner knows the group best with $percent% — ${mode.title}."
        podium.isNotBlank() ->
            "My VibeCheck: $winner comes out on top with $percent% — $podium"
        else ->
            "My VibeCheck: $winner comes out on top with $percent% — ${mode.title}."
    }

    fun french(
        mode: GameMode,
        winner: String,
        percent: Int,
        podium: String,
        fictionalSimulation: Boolean,
    ): String = when {
        fictionalSimulation ->
            "Ma simulation fictive VibeCheck : $winner arrive en tête avec $percent %. " +
                "Les réponses des personnalités publiques sont simulées pour le divertissement et ne représentent pas leurs opinions réelles."
        mode == GameMode.KNOWS_ME ->
            "Mon VibeCheck : $winner connaît le mieux le groupe avec $percent % — ${mode.title}."
        podium.isNotBlank() ->
            "Mon VibeCheck : $winner arrive en tête avec $percent % — $podium"
        else ->
            "Mon VibeCheck : $winner arrive en tête avec $percent % — ${mode.title}."
    }
}
