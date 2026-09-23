package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.LocalStatsStore
import com.vibecheck.app.data.PlayerStat
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.GameEngine
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.domain.model.GameResult
import com.vibecheck.app.domain.model.Vote
import com.vibecheck.app.sharing.ChallengeSharer
import com.vibecheck.app.sharing.ResultCardSharer
import com.vibecheck.app.ui.theme.VibeColors
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    mode: GameMode,
    votes: List<Vote>,
    resultOverride: GameResult? = null,
    challengeTarget: Int?,
    sessionSeed: Long,
    sessionInstanceId: Long,
    intensity: GameIntensity,
    pack: GamePack,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val result = resultOverride ?: GameEngine.result(votes)
    val context = LocalContext.current
    val statsStore = remember(context.applicationContext) {
        LocalStatsStore(context.applicationContext)
    }
    val percent = if (result.total == 0) 0 else (result.score * 100 / result.total)
    val challengeWon = challengeTarget != null && percent >= challengeTarget
    val shareRanking = if (
        mode == GameMode.WHO_OF_US || mode == GameMode.MOST_LIKELY
    ) {
        GameEngine.ranking(votes).take(3)
    } else {
        emptyList()
    }
    val winnerFontSize = when {
        result.winner.length > 18 -> 30.sp
        result.winner.length > 12 -> 36.sp
        else -> 44.sp
    }
    val winnerLineHeight = when {
        result.winner.length > 18 -> 34.sp
        result.winner.length > 12 -> 40.sp
        else -> 48.sp
    }
    val accent = when (mode) {
        GameMode.WHO_OF_US -> VibeColors.Purple
        GameMode.MOST_LIKELY -> VibeColors.Orange
        GameMode.RED_GREEN -> VibeColors.Green
        GameMode.KNOWS_ME -> VibeColors.Blue
    }
    val reveal = remember { Animatable(0f) }
    val score = remember { Animatable(0f) }
    val winnerPulse = remember { Animatable(0f) }
    val challengePulse = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isSharing by remember { mutableStateOf(false) }
    var shareError by remember { mutableStateOf(false) }
    var challengeShareError by remember { mutableStateOf(false) }
    var playerStat by remember(result.winner) {
        mutableStateOf<PlayerStat?>(null)
    }
    val shareInteraction = remember { MutableInteractionSource() }
    val challengeInteraction = remember { MutableInteractionSource() }
    val replayInteraction = remember { MutableInteractionSource() }
    val homeInteraction = remember { MutableInteractionSource() }
    val sharePressed by shareInteraction.collectIsPressedAsState()
    val challengePressed by challengeInteraction.collectIsPressedAsState()
    val replayPressed by replayInteraction.collectIsPressedAsState()
    val homePressed by homeInteraction.collectIsPressedAsState()
    val shareScale by animateFloatAsState(
        targetValue = if (sharePressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = if (sharePressed) 80 else 130),
        label = "shareScale"
    )
    val challengeScale by animateFloatAsState(
        targetValue = if (challengePressed) 0.988f else 1f,
        animationSpec = tween(durationMillis = if (challengePressed) 80 else 130),
        label = "challengeScale"
    )
    val replayScale by animateFloatAsState(
        targetValue = if (replayPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = if (replayPressed) 80 else 130),
        label = "replayScale"
    )
    val homeScale by animateFloatAsState(
        targetValue = if (homePressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = if (homePressed) 80 else 130),
        label = "homeScale"
    )

    LaunchedEffect(sessionInstanceId, result.winner, percent, mode) {
        if (mode != GameMode.RED_GREEN && result.winner.isNotBlank() && result.winner != "Personne") {
            playerStat = statsStore.recordResult(
                sessionKey = mode.name + "_" + sessionInstanceId,
                mode = mode,
                winner = result.winner,
                scorePercent = percent
            )
        }
    }

    LaunchedEffect(percent) {
        reveal.snapTo(0f)
        score.snapTo(0f)
        winnerPulse.snapTo(0f)
        challengePulse.snapTo(0f)
        reveal.animateTo(1f, animationSpec = tween(durationMillis = 280))
        winnerPulse.animateTo(1f, animationSpec = tween(durationMillis = 220))
        winnerPulse.animateTo(0f, animationSpec = tween(durationMillis = 260))
        score.animateTo(percent.toFloat(), animationSpec = tween(durationMillis = 620))
        if (challengeWon) {
            challengePulse.animateTo(1f, animationSpec = tween(durationMillis = 180))
            challengePulse.animateTo(0f, animationSpec = tween(durationMillis = 260))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.graphicsLayer {
                alpha = reveal.value
                translationY = (1f - reveal.value) * 28f
                scaleX = 0.985f + reveal.value * 0.015f
                scaleY = 0.985f + reveal.value * 0.015f
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(accent, CircleShape)
                )
                Text(
                    "VIBECHECK",
                    color = accent,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(18.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xF21B1721)),
                border = BorderStroke(1.dp, accent.copy(alpha = 0.32f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(accent.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
                            .padding(horizontal = 13.dp, vertical = 7.dp)
                    ) {
                        Text(
                            if (mode == GameMode.KNOWS_ME) "QUI CONNAÎT LE MIEUX ?" else "LE GROUPE A PARLÉ",
                            color = accent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.15.sp
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                    Text(
                        result.winner,
                        color = VibeColors.TextPrimary,
                        fontSize = winnerFontSize,
                        lineHeight = winnerLineHeight,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer {
                            val scale = 1f + winnerPulse.value * 0.035f
                            scaleX = scale
                            scaleY = scale
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        score.value.toInt().toString() + "%",
                        color = accent,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        if (mode == GameMode.KNOWS_ME) {
                            "de bonnes réponses • " + mode.title
                        } else {
                            "des réponses • " + mode.title
                        },
                        color = Color(0xFFA9A3B3),
                        textAlign = TextAlign.Center
                    )
                    if (playerStat != null) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            playerStat!!.wins.toString() +
                                if (playerStat!!.wins == 1) " victoire locale" else " victoires locales",
                            color = accent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (playerStat!!.bestScorePercent > 0) {
                            Text(
                                "Meilleur score : " + playerStat!!.bestScorePercent + "%",
                                color = Color(0xFF9D96A7),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))
                    LinearProgressIndicator(
                        progress = { (score.value / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = accent,
                        trackColor = accent.copy(alpha = 0.14f)
                    )

                    if (challengeTarget != null) {
                        Spacer(Modifier.height(18.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (challengeWon) VibeColors.Green.copy(alpha = 0.18f) else accent.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (challengeWon) VibeColors.Green.copy(alpha = 0.5f) else accent.copy(alpha = 0.35f)
                            ),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.graphicsLayer {
                                val scale = 1f + challengePulse.value * 0.025f
                                scaleX = scale
                                scaleY = scale
                            }
                        ) {
                            Text(
                                if (challengeWon) {
                                    if (mode == GameMode.KNOWS_ME) {
                                        "Défi réussi • score cible " + challengeTarget + "% atteint"
                                    } else {
                                        "Défi réussi • consensus " + challengeTarget + "% atteint"
                                    }
                                } else {
                                    if (mode == GameMode.KNOWS_ME) {
                                        "Encore " + (challengeTarget - percent).coerceAtLeast(0) + " points pour atteindre le score cible"
                                    } else {
                                        "Encore " + (challengeTarget - percent).coerceAtLeast(0) + " points pour atteindre le consensus cible"
                                    }
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { alpha = reveal.value },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (!isSharing) {
                        shareError = false
                        isSharing = true
                        scope.launch {
                            try {
                                ResultCardSharer.share(
                                    context = context,
                                    mode = mode,
                                    winner = result.winner,
                                    percent = percent,
                                    intensity = intensity,
                                    pack = pack,
                                    localWins = playerStat?.wins ?: 0,
                                    bestScorePercent = playerStat?.bestScorePercent ?: 0,
                                    ranking = shareRanking,
                                    totalVotes = votes.size
                                )
                            } catch (_: Exception) {
                                shareError = true
                            } finally {
                                isSharing = false
                            }
                        }
                    }
                },
                enabled = !isSharing,
                interactionSource = shareInteraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .graphicsLayer {
                        scaleX = shareScale
                        scaleY = shareScale
                    },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = Color(0xFF161019),
                    disabledContainerColor = accent.copy(alpha = 0.35f),
                    disabledContentColor = Color(0xFF161019).copy(alpha = 0.6f)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Text(
                        if (isSharing) "Préparation..." else "Partager le résultat",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (shareError) {
                Text(
                    "Le partage n'a pas pu être préparé. Réessaie.",
                    color = Color(0xFFFFA3B1),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    challengeShareError = false
                    try {
                        ChallengeSharer.share(
                            context = context,
                            challenge = Challenge(
                                mode = mode,
                                targetPercent = percent,
                                seed = sessionSeed,
                                intensity = intensity,
                                pack = pack
                            )
                        )
                    } catch (_: Exception) {
                        challengeShareError = true
                    }
                },
                interactionSource = challengeInteraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .graphicsLayer {
                        scaleX = challengeScale
                        scaleY = challengeScale
                    },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent.copy(alpha = 0.16f),
                    contentColor = VibeColors.TextPrimary
                )
            ) {
                Text("Défier un ami", fontWeight = FontWeight.Bold)
            }

            if (challengeShareError) {
                Text(
                    "Impossible d'ouvrir le partage du défi. Réessaie.",
                    color = VibeColors.Rose,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onReplay,
                    interactionSource = replayInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = replayScale
                            scaleY = replayScale
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VibeColors.PurpleBright,
                        contentColor = Color(0xFF18121F)
                    )
                ) {
                    Text("Rejouer", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onHome,
                    interactionSource = homeInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = homeScale
                            scaleY = homeScale
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF302A39))
                ) {
                    Text("Modes")
                }
            }
        }
    }
}
