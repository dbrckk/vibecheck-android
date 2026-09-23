package com.vibecheck.app.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vibecheck.app.billing.PremiumBillingManager
import com.vibecheck.app.data.QuestionRepository
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.screens.GameScreen
import com.vibecheck.app.ui.screens.HomeScreen
import com.vibecheck.app.ui.screens.PlayerSetupScreen
import com.vibecheck.app.ui.screens.ResultScreen
import com.vibecheck.app.ui.state.AppScreen
import com.vibecheck.app.ui.state.GameViewModel

@Composable
fun VibeCheckApp(
    incomingChallenge: Challenge? = null,
    onChallengeConsumed: () -> Unit = {},
    gameViewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current
    val billingManager = remember(context.applicationContext) {
        PremiumBillingManager(context.applicationContext)
    }

    DisposableEffect(billingManager) {
        billingManager.start()
        onDispose { billingManager.close() }
    }

    val isPremium by billingManager.isPremium.collectAsState()
    val premiumReady by billingManager.isPurchaseReady.collectAsState()
    val premiumPrice by billingManager.formattedPrice.collectAsState()

    val background = Brush.verticalGradient(
        listOf(Color(0xFF101014), Color(0xFF24153A), Color(0xFF101014))
    )

    val screenName by gameViewModel.screenName.collectAsState()
    val modeName by gameViewModel.modeName.collectAsState()
    val questionIndex by gameViewModel.questionIndex.collectAsState()
    val savedVotes by gameViewModel.savedVotes.collectAsState()
    val players by gameViewModel.players.collectAsState()
    val challengeTargetRaw by gameViewModel.challengeTarget.collectAsState()
    val sessionSeed by gameViewModel.sessionSeed.collectAsState()

    val screen = runCatching { AppScreen.valueOf(screenName) }.getOrDefault(AppScreen.HOME)
    val selectedMode = runCatching { GameMode.valueOf(modeName) }.getOrDefault(GameMode.WHO_OF_US)
    val votes = SessionCodec.decodeVotes(savedVotes)
    val challengeTarget = challengeTargetRaw.takeIf { it != GameViewModel.NO_CHALLENGE }

    LaunchedEffect(incomingChallenge) {
        incomingChallenge?.let { challenge ->
            gameViewModel.acceptChallenge(challenge)
            onChallengeConsumed()
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                when (screen) {
                    AppScreen.HOME -> HomeScreen(
                        isPremium = isPremium,
                        premiumReady = premiumReady,
                        premiumPrice = premiumPrice,
                        onBuyPremium = {
                            (context as? Activity)?.let { activity ->
                                billingManager.launchPurchase(activity)
                            }
                        },
                        onMode = gameViewModel::selectMode
                    )

                    AppScreen.PLAYERS -> PlayerSetupScreen(
                        players = players,
                        challengeTarget = challengeTarget,
                        onBack = gameViewModel::goBackHome,
                        onAddPlayer = gameViewModel::addPlayer,
                        onRemovePlayer = gameViewModel::removePlayer,
                        onStart = gameViewModel::startGame
                    )

                    AppScreen.GAME -> {
                        val questions = QuestionRepository.forMode(
                            mode = selectedMode,
                            seed = sessionSeed
                        )
                        val safeIndex = questionIndex.coerceIn(0, questions.lastIndex)
                        val question = questions[safeIndex]

                        GameScreen(
                            mode = selectedMode,
                            questionText = question.text,
                            progress = safeIndex + 1,
                            total = questions.size,
                            answers = if (selectedMode == GameMode.RED_GREEN) {
                                listOf("Green Flag", "Red Flag")
                            } else {
                                players
                            },
                            onAnswer = { answer ->
                                gameViewModel.answer(
                                    questionId = question.id,
                                    answer = answer,
                                    isLastQuestion = safeIndex == questions.lastIndex
                                )
                            },
                            onExit = gameViewModel::abandonGame
                        )
                    }

                    AppScreen.RESULT -> ResultScreen(
                        mode = selectedMode,
                        votes = votes,
                        challengeTarget = challengeTarget,
                        sessionSeed = sessionSeed,
                        onReplay = gameViewModel::replay,
                        onHome = gameViewModel::goHome
                    )
                }
            }
        }
    }
}
