package com.vibecheck.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vibecheck.app.data.QuestionRepository
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Vote
import com.vibecheck.app.ui.screens.GameScreen
import com.vibecheck.app.ui.screens.HomeScreen
import com.vibecheck.app.ui.screens.PlayerSetupScreen
import com.vibecheck.app.ui.screens.ResultScreen

private enum class Screen { HOME, PLAYERS, GAME, RESULT }

@Composable
fun VibeCheckApp(
    incomingChallenge: Challenge? = null,
    onChallengeConsumed: () -> Unit = {}
) {
    val background = Brush.verticalGradient(
        listOf(Color(0xFF101014), Color(0xFF24153A), Color(0xFF101014))
    )

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                var screenName by rememberSaveable { mutableStateOf(Screen.HOME.name) }
                var modeName by rememberSaveable { mutableStateOf(GameMode.WHO_OF_US.name) }
                var questionIndex by rememberSaveable { mutableIntStateOf(0) }
                var savedVotes by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
                var players by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
                var challengeTarget by rememberSaveable { mutableStateOf<Int?>(null) }
                var sessionSeed by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

                val screen = runCatching { Screen.valueOf(screenName) }.getOrDefault(Screen.HOME)
                val selectedMode = runCatching { GameMode.valueOf(modeName) }.getOrDefault(GameMode.WHO_OF_US)
                val votes = SessionCodec.decodeVotes(savedVotes)

                LaunchedEffect(incomingChallenge) {
                    incomingChallenge?.let { challenge ->
                        modeName = challenge.mode.name
                        challengeTarget = challenge.targetPercent
                        questionIndex = 0
                        savedVotes = arrayListOf()
                        screenName = Screen.PLAYERS.name
                        onChallengeConsumed()
                    }
                }

                when (screen) {
                    Screen.HOME -> HomeScreen { mode ->
                        modeName = mode.name
                        challengeTarget = null
                        questionIndex = 0
                        savedVotes = arrayListOf()
                        screenName = Screen.PLAYERS.name
                    }

                    Screen.PLAYERS -> PlayerSetupScreen(
                        players = players,
                        challengeTarget = challengeTarget,
                        onBack = { screenName = Screen.HOME.name },
                        onAddPlayer = { player ->
                            players = ArrayList(players + player)
                        },
                        onRemovePlayer = { player ->
                            players = ArrayList(players.filterNot { it == player })
                        },
                        onStart = {
                            savedVotes = arrayListOf()
                            questionIndex = 0
                            sessionSeed = System.currentTimeMillis()
                            screenName = Screen.GAME.name
                        }
                    )

                    Screen.GAME -> {
                        val questions = QuestionRepository.forMode(
                            mode = selectedMode,
                            seed = sessionSeed
                        )
                        val question = questions[questionIndex]
                        GameScreen(
                            mode = selectedMode,
                            questionText = question.text,
                            progress = questionIndex + 1,
                            total = questions.size,
                            answers = if (selectedMode == GameMode.RED_GREEN) {
                                listOf("Green Flag", "Red Flag")
                            } else {
                                players
                            },
                            onAnswer = { answer ->
                                val updatedVotes = votes + Vote(question.id, answer)
                                savedVotes = ArrayList(SessionCodec.encodeVotes(updatedVotes))
                                if (questionIndex == questions.lastIndex) {
                                    screenName = Screen.RESULT.name
                                } else {
                                    questionIndex += 1
                                }
                            }
                        )
                    }

                    Screen.RESULT -> ResultScreen(
                        mode = selectedMode,
                        votes = votes,
                        challengeTarget = challengeTarget,
                        onReplay = {
                            savedVotes = arrayListOf()
                            questionIndex = 0
                            sessionSeed = System.currentTimeMillis()
                            screenName = Screen.GAME.name
                        },
                        onHome = {
                            savedVotes = arrayListOf()
                            screenName = Screen.HOME.name
                        }
                    )
                }
            }
        }
    }
}
