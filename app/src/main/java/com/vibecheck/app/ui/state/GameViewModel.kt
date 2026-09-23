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
    val knowSecretAnswer = savedStateHandle.getStateFlow(KEY_KNOW_SECRET, "")
    val knowGuesserIndex = savedStateHandle.getStateFlow(KEY_KNOW_GUESSER_INDEX, 0)
    val knowScores = savedStateHandle.getStateFlow(KEY_KNOW_SCORES, arrayListOf<Int>())

    fun acceptChallenge(challenge: Challenge) {
        savedStateHandle[KEY_MODE] = challenge.mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = challenge.targetPercent
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_SESSION_SEED] = challenge.seed
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = initialScreenFor(challenge.mode).name
    }

    fun selectMode(mode: GameMode) {
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        resetKnowMeState()
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
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    fun startGame() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        resetKnowMeState()
        if (challengeTarget.value == NO_CHALLENGE) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
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

    fun setKnowMeSecret(answer: String) {
        if (modeName.value != GameMode.KNOWS_ME.name || answer.isBlank()) return
        if (knowSecretAnswer.value.isNotBlank()) return
        savedStateHandle[KEY_KNOW_SECRET] = answer
        savedStateHandle[KEY_KNOW_GUESSER_INDEX] = 0
        ensureKnowScoreSlots()
    }

    fun submitKnowMeGuess(answer: String, isLastQuestion: Boolean) {
        if (modeName.value != GameMode.KNOWS_ME.name) return
        val secret = knowSecretAnswer.value
        val guessers = players.value.drop(1)
        if (secret.isBlank() || guessers.isEmpty()) return

        ensureKnowScoreSlots()
        val index = knowGuesserIndex.value.coerceIn(0, guessers.lastIndex)
        val scores = ArrayList(knowScores.value)
        if (answer == secret) {
            scores[index] = scores[index] + 1
        }
        savedStateHandle[KEY_KNOW_SCORES] = scores

        if (index < guessers.lastIndex) {
            savedStateHandle[KEY_KNOW_GUESSER_INDEX] = index + 1
        } else if (isLastQuestion) {
            savedStateHandle[KEY_SCREEN] = AppScreen.RESULT.name
        } else {
            savedStateHandle[KEY_QUESTION_INDEX] = questionIndex.value + 1
            savedStateHandle[KEY_KNOW_SECRET] = ""
            savedStateHandle[KEY_KNOW_GUESSER_INDEX] = 0
        }
    }

    fun abandonGame() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    fun replay() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        resetKnowMeState()
        if (challengeTarget.value == NO_CHALLENGE) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun goHome() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    private fun ensureKnowScoreSlots() {
        val needed = players.value.drop(1).size
        if (knowScores.value.size != needed) {
            savedStateHandle[KEY_KNOW_SCORES] = ArrayList(List(needed) { 0 })
        }
    }

    private fun resetKnowMeState() {
        savedStateHandle[KEY_KNOW_SECRET] = ""
        savedStateHandle[KEY_KNOW_GUESSER_INDEX] = 0
        savedStateHandle[KEY_KNOW_SCORES] = arrayListOf<Int>()
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
        private const val KEY_KNOW_SECRET = "know_secret"
        private const val KEY_KNOW_GUESSER_INDEX = "know_guesser_index"
        private const val KEY_KNOW_SCORES = "know_scores"
    }
}
