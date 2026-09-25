package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.PlayerRules
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.domain.model.Vote
import com.vibecheck.app.domain.solo.PersonaAnswer
import com.vibecheck.app.domain.solo.PersonaAnswer

enum class AppScreen { HOME, PLAYERS, GAME, RESULT, LEADERBOARD }

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
    val sessionInstanceId = savedStateHandle.getStateFlow(KEY_SESSION_INSTANCE_ID, System.nanoTime())
    val intensityName = savedStateHandle.getStateFlow(KEY_INTENSITY, GameIntensity.NORMAL.name)
    val packName = savedStateHandle.getStateFlow(KEY_PACK, GamePack.MIX.name)
    val legacyChallenge = savedStateHandle.getStateFlow(KEY_LEGACY_CHALLENGE, false)
    val knowSecretAnswer = savedStateHandle.getStateFlow(KEY_KNOW_SECRET, "")
    val knowGuesserIndex = savedStateHandle.getStateFlow(KEY_KNOW_GUESSER_INDEX, 0)
    val knowScores = savedStateHandle.getStateFlow(KEY_KNOW_SCORES, arrayListOf<Int>())
    val knowHandoffPending = savedStateHandle.getStateFlow(KEY_KNOW_HANDOFF, false)
    val isSoloSession = savedStateHandle.getStateFlow(KEY_IS_SOLO_SESSION, false)
    val soloPersonaIds = savedStateHandle.getStateFlow(KEY_SOLO_PERSONA_IDS, arrayListOf<String>())
    val isSoloSession = savedStateHandle.getStateFlow(KEY_IS_SOLO_SESSION, false)
    val soloPersonaIds = savedStateHandle.getStateFlow(KEY_SOLO_PERSONA_IDS, arrayListOf<String>())

    fun startSoloSession(
        personaIds: List<String>,
        mode: GameMode,
        seed: Long,
    ) {
        val normalizedIds = personaIds.distinct()
        if (normalizedIds.size !in SOLO_MIN_COMPANIONS..SOLO_MAX_COMPANIONS) return

        savedStateHandle[KEY_IS_SOLO_SESSION] = true
        savedStateHandle[KEY_SOLO_PERSONA_IDS] = ArrayList(normalizedIds)
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_SESSION_SEED] = seed
        savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun submitSoloRound(
        questionId: String,
        answers: List<PersonaAnswer>,
        isLastQuestion: Boolean,
    ) {
        if (!isSoloSession.value || answers.isEmpty()) return
        if (screenName.value == AppScreen.RESULT.name) return

        val currentVotes = SessionCodec.decodeVotes(savedVotes.value)
        if (currentVotes.any { it.questionId == questionId }) return

        val winner = answers
            .groupBy { it.option }
            .map { (option, optionAnswers) ->
                Triple(
                    option,
                    optionAnswers.size,
                    optionAnswers.sumOf { it.confidence },
                )
            }
            .sortedWith(
                compareByDescending<Triple<String, Int, Int>> { it.second }
                    .thenByDescending { it.third }
                    .thenBy { it.first }
            )
            .first()
            .first

        answer(
            questionId = questionId,
            answer = winner,
            isLastQuestion = isLastQuestion,
        )
    }

    fun acceptChallenge(challenge: Challenge) {
        clearSoloState()
        clearSoloSession()
        savedStateHandle[KEY_MODE] = challenge.mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = challenge.targetPercent
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_SESSION_SEED] = challenge.seed
        savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
        savedStateHandle[KEY_INTENSITY] = (challenge.intensity ?: GameIntensity.NORMAL).name
        savedStateHandle[KEY_PACK] = (challenge.pack ?: GamePack.MIX).name
        savedStateHandle[KEY_LEGACY_CHALLENGE] = challenge.intensity == null
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = initialScreenFor(challenge.mode).name
    }

    fun selectMode(mode: GameMode) {
        clearSoloState()
        clearSoloSession()
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = initialScreenFor(mode).name
        if (mode == GameMode.RED_GREEN) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
    }

    fun quickStartMode(mode: GameMode) {
        clearSoloState()
        clearSoloSession()
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        resetKnowMeState()

        val canStartImmediately = mode == GameMode.RED_GREEN || PlayerRules.canStart(players.value)
        if (canStartImmediately) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
            savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
            savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
        } else {
            savedStateHandle[KEY_SCREEN] = AppScreen.PLAYERS.name
        }
    }

    fun openLeaderboard() {
        savedStateHandle[KEY_SCREEN] = AppScreen.LEADERBOARD.name
    }

    fun editPlayers() {
        clearSoloState()
        clearSoloSession()
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.PLAYERS.name
    }

    fun selectIntensity(intensity: GameIntensity) {
        if (challengeTarget.value != NO_CHALLENGE) return
        savedStateHandle[KEY_INTENSITY] = intensity.name
    }

    fun selectPack(pack: GamePack) {
        if (challengeTarget.value != NO_CHALLENGE) return
        savedStateHandle[KEY_PACK] = pack.name
    }

    fun restorePlayers(restoredPlayers: List<String>) {
        if (players.value.isNotEmpty()) return
        savedStateHandle[KEY_PLAYERS] = ArrayList(restoredPlayers)
    }

    fun addPlayer(player: String) {
        savedStateHandle[KEY_PLAYERS] = ArrayList(players.value + player)
    }

    fun removePlayer(player: String) {
        savedStateHandle[KEY_PLAYERS] = ArrayList(players.value.filterNot { it == player })
    }

    fun goBackHome() {
        clearSoloSession()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    fun startGame() {
        clearSoloState()
        clearSoloSession()
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        resetKnowMeState()
        if (challengeTarget.value == NO_CHALLENGE) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
        savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun startSoloSession(
        personaIds: List<String>,
        mode: GameMode = GameMode.WHO_OF_US,
        seed: Long = System.currentTimeMillis(),
    ) {
        val validIds = personaIds
            .filter { it.isNotBlank() }
            .distinct()
        if (validIds.size !in MIN_SOLO_COMPANIONS..MAX_SOLO_COMPANIONS) return
        if (mode == GameMode.KNOWS_ME) return

        savedStateHandle[KEY_IS_SOLO_SESSION] = true
        savedStateHandle[KEY_SOLO_PERSONA_IDS] = ArrayList(validIds)
        savedStateHandle[KEY_MODE] = mode.name
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_LEGACY_CHALLENGE] = false
        savedStateHandle[KEY_SESSION_SEED] = seed
        savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun submitSoloRound(
        questionId: String,
        answers: List<PersonaAnswer>,
        isLastQuestion: Boolean,
    ) {
        if (!isSoloSession.value || screenName.value != AppScreen.GAME.name) return
        if (answers.isEmpty()) return

        val majority = answers
            .groupingBy { it.option }
            .eachCount()
            .entries
            .sortedWith(
                compareByDescending<Map.Entry<String, Int>> { it.value }
                    .thenBy { it.key }
            )
            .first()
            .key

        answer(
            questionId = questionId,
            answer = majority,
            isLastQuestion = isLastQuestion,
        )
    }

    fun answer(questionId: String, answer: String, isLastQuestion: Boolean) {
        if (screenName.value == AppScreen.RESULT.name) return
        val currentVotes = SessionCodec.decodeVotes(savedVotes.value)
        if (currentVotes.any { it.questionId == questionId }) return

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
        savedStateHandle[KEY_KNOW_HANDOFF] = true
        ensureKnowScoreSlots()
    }

    fun submitKnowMeGuess(answer: String, isLastQuestion: Boolean) {
        if (modeName.value != GameMode.KNOWS_ME.name) return
        if (screenName.value == AppScreen.RESULT.name) return
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
            savedStateHandle[KEY_KNOW_HANDOFF] = true
        } else if (isLastQuestion) {
            savedStateHandle[KEY_SCREEN] = AppScreen.RESULT.name
        } else {
            savedStateHandle[KEY_QUESTION_INDEX] = questionIndex.value + 1
            savedStateHandle[KEY_KNOW_SECRET] = ""
            savedStateHandle[KEY_KNOW_GUESSER_INDEX] = 0
            savedStateHandle[KEY_KNOW_HANDOFF] = true
        }
    }

    fun confirmKnowMeHandoff() {
        if (modeName.value != GameMode.KNOWS_ME.name) return
        savedStateHandle[KEY_KNOW_HANDOFF] = false
    }

    fun abandonGame() {
        clearSoloSession()
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        savedStateHandle[KEY_CHALLENGE_TARGET] = NO_CHALLENGE
        resetKnowMeState()
        savedStateHandle[KEY_SCREEN] = AppScreen.HOME.name
    }

    fun replay() {
        savedStateHandle[KEY_VOTES] = arrayListOf<String>()
        savedStateHandle[KEY_QUESTION_INDEX] = 0
        resetKnowMeState()
        if (challengeTarget.value == NO_CHALLENGE) {
            savedStateHandle[KEY_SESSION_SEED] = System.currentTimeMillis()
        }
        savedStateHandle[KEY_SESSION_INSTANCE_ID] = System.nanoTime()
        savedStateHandle[KEY_SCREEN] = AppScreen.GAME.name
    }

    fun goHome() {
        clearSoloSession()
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

    private fun clearSoloSession() {
        savedStateHandle[KEY_IS_SOLO_SESSION] = false
        savedStateHandle[KEY_SOLO_PERSONA_IDS] = arrayListOf<String>()
    }

    private fun clearSoloState() {
        savedStateHandle[KEY_IS_SOLO_SESSION] = false
        savedStateHandle[KEY_SOLO_PERSONA_IDS] = arrayListOf<String>()
    }

    private fun resetKnowMeState() {
        savedStateHandle[KEY_KNOW_SECRET] = ""
        savedStateHandle[KEY_KNOW_GUESSER_INDEX] = 0
        savedStateHandle[KEY_KNOW_SCORES] = arrayListOf<Int>()
        savedStateHandle[KEY_KNOW_HANDOFF] = false
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
        private const val KEY_SESSION_INSTANCE_ID = "session_instance_id"
        private const val KEY_INTENSITY = "intensity"
        private const val KEY_PACK = "pack"
        private const val KEY_LEGACY_CHALLENGE = "legacy_challenge"
        private const val KEY_KNOW_SECRET = "know_secret"
        private const val KEY_KNOW_GUESSER_INDEX = "know_guesser_index"
        private const val KEY_KNOW_SCORES = "know_scores"
        private const val KEY_KNOW_HANDOFF = "know_handoff"
        private const val KEY_IS_SOLO_SESSION = "is_solo_session"
        private const val KEY_SOLO_PERSONA_IDS = "solo_persona_ids"
        private const val SOLO_MIN_COMPANIONS = 2
        private const val SOLO_MAX_COMPANIONS = 7
        private const val KEY_IS_SOLO_SESSION = "is_solo_session"
        private const val KEY_SOLO_PERSONA_IDS = "solo_persona_ids"
        private const val MIN_SOLO_COMPANIONS = 2
        private const val MAX_SOLO_COMPANIONS = 7
    }
}
