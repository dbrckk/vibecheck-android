package com.vibecheck.app.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vibecheck.app.billing.PremiumBillingManager
import com.vibecheck.app.billing.PurchaseStatus
import com.vibecheck.app.data.KnowMeRepository
import com.vibecheck.app.data.LocalStatsStore
import com.vibecheck.app.data.OnboardingStore
import com.vibecheck.app.data.PlayerGroupStore
import com.vibecheck.app.data.QuestionHistoryStore
import com.vibecheck.app.data.QuestionRepository
import com.vibecheck.app.data.ThemeStore
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GameResult
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.ui.screens.GameScreen
import com.vibecheck.app.ui.screens.HomeScreen
import com.vibecheck.app.ui.screens.LeaderboardScreen
import com.vibecheck.app.ui.screens.OnboardingScreen
import com.vibecheck.app.ui.screens.PassPhoneScreen
import com.vibecheck.app.ui.screens.PlayerSetupScreen
import com.vibecheck.app.ui.screens.ResultScreen
import com.vibecheck.app.ui.state.AppScreen
import com.vibecheck.app.ui.state.GameViewModel
import com.vibecheck.app.ui.theme.VibeBackdrop
import com.vibecheck.app.ui.theme.VibeCheckTheme
import com.vibecheck.app.ui.theme.VibeColors
import com.vibecheck.app.ui.theme.VibeThemeStyle

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
    val questionHistory = remember(context.applicationContext) {
        QuestionHistoryStore(context.applicationContext)
    }
    val playerGroupStore = remember(context.applicationContext) {
        PlayerGroupStore(context.applicationContext)
    }
    val localStatsStore = remember(context.applicationContext) {
        LocalStatsStore(context.applicationContext)
    }
    val onboardingStore = remember(context.applicationContext) {
        OnboardingStore(context.applicationContext)
    }
    val themeStore = remember(context.applicationContext) {
        ThemeStore(context.applicationContext)
    }
    var themeStyle by remember {
        mutableStateOf(themeStore.load())
    }
    var showOnboarding by remember {
        mutableStateOf(!onboardingStore.isCompleted())
    }
    var groupHydrated by remember { mutableStateOf(false) }

    DisposableEffect(billingManager) {
        billingManager.start()
        onDispose { billingManager.close() }
    }

    val isPremium by billingManager.isPremium.collectAsState()
    val premiumReady by billingManager.isPurchaseReady.collectAsState()
    val premiumPrice by billingManager.formattedPrice.collectAsState()
    val purchaseStatus by billingManager.purchaseStatus.collectAsState()

    val screenName by gameViewModel.screenName.collectAsState()
    val modeName by gameViewModel.modeName.collectAsState()
    val questionIndex by gameViewModel.questionIndex.collectAsState()
    val savedVotes by gameViewModel.savedVotes.collectAsState()
    val players by gameViewModel.players.collectAsState()
    val challengeTargetRaw by gameViewModel.challengeTarget.collectAsState()
    val sessionSeed by gameViewModel.sessionSeed.collectAsState()
    val sessionInstanceId by gameViewModel.sessionInstanceId.collectAsState()
    val intensityName by gameViewModel.intensityName.collectAsState()
    val packName by gameViewModel.packName.collectAsState()
    val legacyChallenge by gameViewModel.legacyChallenge.collectAsState()
    val knowSecretAnswer by gameViewModel.knowSecretAnswer.collectAsState()
    val knowGuesserIndex by gameViewModel.knowGuesserIndex.collectAsState()
    val knowScores by gameViewModel.knowScores.collectAsState()
    val knowHandoffPending by gameViewModel.knowHandoffPending.collectAsState()

    val screen = runCatching { AppScreen.valueOf(screenName) }.getOrDefault(AppScreen.HOME)
    val selectedMode = runCatching { GameMode.valueOf(modeName) }.getOrDefault(GameMode.WHO_OF_US)
    val selectedIntensity = runCatching {
        GameIntensity.valueOf(intensityName)
    }.getOrDefault(GameIntensity.NORMAL)
    val selectedPack = runCatching {
        GamePack.valueOf(packName)
    }.getOrDefault(GamePack.MIX)
    val votes = SessionCodec.decodeVotes(savedVotes)
    val challengeTarget = challengeTargetRaw.takeIf { it != GameViewModel.NO_CHALLENGE }
    val avoidedIds = if (challengeTarget == null) {
        questionHistory.recentIds(selectedMode)
    } else {
        emptySet()
    }
    val sessionIntensity = if (legacyChallenge) null else selectedIntensity
    val sessionPack = if (legacyChallenge) GamePack.MIX else selectedPack
    val groupLeader = if (screen == AppScreen.HOME) {
        players
            .map(localStatsStore::statFor)
            .filter { it.wins > 0 }
            .sortedWith(
                compareByDescending<com.vibecheck.app.data.PlayerStat> { it.wins }
                    .thenByDescending { it.bestScorePercent }
                    .thenBy { it.name }
            )
            .firstOrNull()
    } else {
        null
    }

    LaunchedEffect(Unit) {
        val restored = playerGroupStore.load()
        if (restored.isNotEmpty()) {
            gameViewModel.restorePlayers(restored)
        }
        groupHydrated = true
    }

    LaunchedEffect(players, groupHydrated) {
        if (groupHydrated) {
            playerGroupStore.save(players)
        }
    }

    LaunchedEffect(incomingChallenge) {
        incomingChallenge?.let { challenge ->
            gameViewModel.acceptChallenge(challenge)
            onChallengeConsumed()
        }
    }

    LaunchedEffect(screen, selectedMode, sessionSeed) {
        if (screen == AppScreen.RESULT && challengeTarget == null) {
            val ids = if (selectedMode == GameMode.KNOWS_ME) {
                KnowMeRepository.forSeed(
                    seed = sessionSeed,
                    avoidIds = questionHistory.recentIds(selectedMode),
                    intensity = sessionIntensity,
                pack = sessionPack
                ).map { it.id }
            } else {
                QuestionRepository.forMode(
                    mode = selectedMode,
                    seed = sessionSeed,
                    avoidIds = questionHistory.recentIds(selectedMode),
                    intensity = sessionIntensity,
                pack = sessionPack
                ).map { it.id }
            }
            questionHistory.remember(selectedMode, ids)
        }
    }

    BackHandler(enabled = screen != AppScreen.HOME) {
        when (screen) {
            AppScreen.PLAYERS -> gameViewModel.goBackHome()
            AppScreen.GAME -> gameViewModel.abandonGame()
            AppScreen.RESULT -> gameViewModel.goHome()
            AppScreen.LEADERBOARD -> gameViewModel.goHome()
            AppScreen.HOME -> Unit
        }
    }

    VibeCheckTheme(style = themeStyle) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            VibeBackdrop(style = themeStyle) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                if (showOnboarding && incomingChallenge == null && screen == AppScreen.HOME) {
                    OnboardingScreen(
                        onStart = {
                            onboardingStore.markCompleted()
                            showOnboarding = false
                        }
                    )
                } else {
                AnimatedContent(
                    targetState = screen,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220)) +
                            scaleIn(initialScale = 0.985f, animationSpec = tween(220)))
                            .togetherWith(fadeOut(animationSpec = tween(150)))
                    },
                    label = "screenTransition"
                ) { animatedScreen ->
                    when (animatedScreen) {
                    AppScreen.HOME -> HomeScreen(
                        isPremium = isPremium,
                        premiumReady = premiumReady,
                        premiumPrice = premiumPrice,
                        purchaseStatus = purchaseStatus,
                        selectedIntensity = selectedIntensity,
                        selectedPack = selectedPack,
                        savedPlayerCount = players.size,
                        groupLeaderName = groupLeader?.name,
                        groupLeaderWins = groupLeader?.wins ?: 0,
                        onOpenLeaderboard = gameViewModel::openLeaderboard,
                        onEditGroup = gameViewModel::editPlayers,
                        onBuyPremium = {
                            (context as? Activity)?.let { activity ->
                                billingManager.launchPurchase(activity)
                            }
                        },
                        onIntensity = gameViewModel::selectIntensity,
                        onPack = gameViewModel::selectPack,
                        onMode = gameViewModel::quickStartMode
                    )

                    AppScreen.PLAYERS -> PlayerSetupScreen(
                        mode = selectedMode,
                        players = players,
                        challengeTarget = challengeTarget,
                        onBack = gameViewModel::goBackHome,
                        onAddPlayer = gameViewModel::addPlayer,
                        onRemovePlayer = gameViewModel::removePlayer,
                        onStart = gameViewModel::startGame
                    )

                    AppScreen.GAME -> {
                        if (selectedMode == GameMode.KNOWS_ME) {
                            val prompts = KnowMeRepository.forSeed(
                                seed = sessionSeed,
                                avoidIds = avoidedIds,
                                intensity = sessionIntensity,
                            pack = sessionPack
                            )
                            val target = players.firstOrNull()
                            val guessers = players.drop(1)

                            if (prompts.isEmpty() || target == null || guessers.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Ajoute au moins 2 joueurs pour ce mode.",
                                        color = VibeColors.TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                val safeIndex = questionIndex.coerceIn(0, prompts.lastIndex)
                                val currentPrompt = prompts[safeIndex]
                                val secretPhase = knowSecretAnswer.isBlank()
                                val safeGuesserIndex = knowGuesserIndex.coerceIn(0, guessers.lastIndex)
                                val guesser = guessers[safeGuesserIndex]
                                val promptText = if (secretPhase) {
                                    target + ", réponds secrètement.\n\n" + currentPrompt.text
                                } else {
                                    guesser + ", que choisirait " + target + " ?\n\n" + currentPrompt.text
                                }

                                if (knowHandoffPending) {
                                    val nextPlayer = if (secretPhase) target else guesser
                                    val handoffLabel = if (secretPhase) {
                                        "La réponse précédente est masquée. $target doit répondre avant de repasser le téléphone."
                                    } else {
                                        "La réponse secrète est masquée. Ne continue que lorsque $guesser tient le téléphone."
                                    }
                                    PassPhoneScreen(
                                        nextPlayer = nextPlayer,
                                        promptLabel = handoffLabel,
                                        onReady = gameViewModel::confirmKnowMeHandoff
                                    )
                                } else {
                                    GameScreen(
                                        mode = selectedMode,
                                        questionText = promptText,
                                        progress = safeIndex + 1,
                                        total = prompts.size,
                                        answers = currentPrompt.options,
                                        onAnswer = { answer ->
                                            if (secretPhase) {
                                                gameViewModel.setKnowMeSecret(answer)
                                            } else {
                                                gameViewModel.submitKnowMeGuess(
                                                    answer = answer,
                                                    isLastQuestion = safeIndex == prompts.lastIndex
                                                )
                                            }
                                        },
                                        onExit = gameViewModel::abandonGame
                                    )
                                }
                            }
                        } else {
                            val questions = QuestionRepository.forMode(
                                mode = selectedMode,
                                seed = sessionSeed,
                                avoidIds = avoidedIds,
                                intensity = sessionIntensity,
                            pack = sessionPack
                            )

                            if (questions.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.layout.Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Questions indisponibles",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Ce mode n'a actuellement aucune question.",
                                            color = Color(0xFFAAA2B5)
                                        )
                                        androidx.compose.foundation.layout.Spacer(
                                            Modifier.height(12.dp)
                                        )
                                        Button(onClick = gameViewModel::abandonGame) {
                                            Text("Retour aux modes")
                                        }
                                    }
                                }
                            } else {
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
                        }
                    }

                    AppScreen.LEADERBOARD -> {
                        val leaderboard = players
                            .map(localStatsStore::statFor)
                            .sortedWith(
                                compareByDescending<com.vibecheck.app.data.PlayerStat> { it.wins }
                                    .thenByDescending { it.bestScorePercent }
                                    .thenBy { it.name }
                            )
                        LeaderboardScreen(
                            stats = leaderboard,
                            onBack = gameViewModel::goHome
                        )
                    }

                    AppScreen.RESULT -> {
                        val knowMeResult = if (selectedMode == GameMode.KNOWS_ME) {
                            val guessers = players.drop(1)
                            val ranking = guessers.mapIndexed { index, name ->
                                name to (knowScores.getOrNull(index) ?: 0)
                            }.sortedWith(
                                compareByDescending<Pair<String, Int>> { it.second }
                                    .thenBy { it.first }
                            )
                            val best = ranking.firstOrNull()
                            GameResult(
                                winner = best?.first ?: "Personne",
                                score = best?.second ?: 0,
                                total = KnowMeRepository.forSeed(
                                    seed = sessionSeed,
                                    avoidIds = avoidedIds,
                                    intensity = sessionIntensity,
                                pack = sessionPack
                                ).size
                            )
                        } else {
                            null
                        }

                        ResultScreen(
                            mode = selectedMode,
                            votes = votes,
                            resultOverride = knowMeResult,
                            challengeTarget = challengeTarget,
                            sessionSeed = sessionSeed,
                            sessionInstanceId = sessionInstanceId,
                            intensity = selectedIntensity,
                            pack = selectedPack,
                            onReplay = gameViewModel::replay,
                            onHome = gameViewModel::goHome
                        )
                    }
                    }
                }
                }
                if (!showOnboarding && screen == AppScreen.HOME) {
                    Button(
                        onClick = {
                            val styles = VibeThemeStyle.entries
                            val next = styles[(styles.indexOf(themeStyle) + 1) % styles.size]
                            themeStyle = next
                            themeStore.save(next)
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Text(themeStyle.label)
                    }
                }
                }
            }
        }
    }
}
