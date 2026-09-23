package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Vote

enum class AppScreen { HOME, PLAYERS, GAME, RESULT }

class GameViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val screenName = savedStateHandle.getStateFlow(KEY_SCREEN, AppScreen.HOME.name)
    val modeName = savedStateHandle.getStateFlow(KEY_MODE, GameMode.WHO_OF_US.name)
    val questionIndex = savedStateHandle.getStateFlow(KEY_QUESTION_INDEX, 0)
    val savedVotes = savedStateHandle.getStateFlow(KEY_VOTES, arrayListOf<String>())
    val players = savedStateHandle.getStateFlow(KEY_PLAYERS, arrayListOf<String>())
    val challengeTarget = savedStateHandle.getStateFlow(KEY_CHALLENGE_TARGET, NO_CHALLENGE)
    val sessionSeed = savedStateHandle.getStateFlow(KEY_SESSION_SEED, System.currentTimeMillis())

    fun acceptChallenge(challenge: Challenge) {
        savedStateHandle[KEY_MODE] = challenge.mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = challenge.targetPercent
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_SCREEN] = initialScreenFor(challenge.mode).name
        if (challenge.mode == GameMode.RED_GREEN) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
    }

    fun selectMode(mode: GameMode) {
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_SCREEN] = initialScreenFor(mode).name
        if (mode == GameMode.RED_GREEN) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
    }

    fun addPlayer(player: String) {
        savedStateHandle[KEY_PLAYERS] = ArrayList(players.value + player)
    }

    fun removePlayer(player: String) {
        savedStateHandle[KEY_PLAYERS] = ArrayList(players.value.filterNot { it == player })
    }

    fun goBackHome() {
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    fun startGame() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun answer(questionId: String, answer: String, isLastQuestion: Boolean) {
        val currentVotes = SessionCodec.decodeVotes(savedVotes.value)
        val updatedVotes = currentVotes + Vote(questionId, answer)
        savedStateHandle[KEY_VOTES] = ArrayList(SessionCodec.encodeVotes(updatedVotes))

        if (isLastQuestion) {
            savedStateHandle[KEY_SCREEN] = AppScreen.RESULT.name
        } else {
            savedStateHandle[KEY_QUESTION_INDEX] = questionIndex.value + 1
        }
    }

    fun replay() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun goHome() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    private fun initialScreenFor(mode: GameMode): AppScreen =
        if (mode == GameMode.RED_GREEN) AppScreen.GAME else AppScreen.PLAYERS

    companion object {
        const val NO_CHALLENGE = -1

        private const val KEY_SCREEN = "screen"
        private const val KEY_MODE = "mode"
        private const val KEY_QUESTION_INDEX = "question_index"
        private const val KEY_VOTES = "votes"
        private const val KEY_PLAYERS = "players"
        private const val KEY_CHALLENGE_TARGET = "challenge_target"
        private const val KEY_SESSION_SEED = "session_seed"
    }
}
