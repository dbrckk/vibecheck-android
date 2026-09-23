package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.GameEngine
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.Vote
import com.vibecheck.app.sharing.ChallengeSharer
import com.vibecheck.app.sharing.ResultCardSharer
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    mode: GameMode,
    votes: List<Vote>,
    challengeTarget: Int?,
    sessionSeed: Long,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val result = GameEngine.result(votes)
    val context = LocalContext.current
    val percent = if (result.total == 0) 0 else (result.score * 100 / result.total)
    val challengeWon = challengeTarget != null && percent >= challengeTarget
    val reveal = remember { Animatable(0f) }
    val score = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isSharing by remember { mutableStateOf(false) }
    var shareError by remember { mutableStateOf(false) }

    LaunchedEffect(percent) {
        reveal.snapTo(0f)
        score.snapTo(0f)
        reveal.animateTo(1f, animationSpec = tween(durationMillis = 280))
        score.animateTo(percent.toFloat(), animationSpec = tween(durationMillis = 620))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
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
            Text(
                "VIBECHECK",
                color = Color(0xFFC9A7FF),
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(18.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xE6211E29)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "LE GROUPE A PARLÉ",
                        color = Color(0xFF9E91AE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(18.dp))
                    Text(
                        result.winner,
                        color = Color.White,
                        fontSize = 42.sp,
                        lineHeight = 46.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        score.value.toInt().toString() + "%",
                        color = Color(0xFFE8D8FF),
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "des réponses • " + mode.title,
                        color = Color(0xFFA9A3B3),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(18.dp))
                    LinearProgressIndicator(
                        progress = { (score.value / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color(0xFFC9A7FF),
                        trackColor = Color(0xFF3A3342)
                    )

                    if (challengeTarget != null) {
                        Spacer(Modifier.height(18.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (challengeWon) Color(0xFF294337) else Color(0xFF3B3044)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                if (challengeWon) {
                                    "Défi réussi • objectif " + challengeTarget + "%"
                                } else {
                                    "Encore " + (challengeTarget - percent).coerceAtLeast(0) + " points pour battre le défi"
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
                                    percent = percent
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
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp)
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
                    ChallengeSharer.share(
                        context = context,
                        challenge = Challenge(mode = mode, targetPercent = percent, seed = sessionSeed)
                    )
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B2E6B))
            ) {
                Text("Défier un ami", fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onReplay,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEE6FF), contentColor = Color(0xFF18121F))
                ) {
                    Text("Rejouer", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onHome,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF302A39))
                ) {
                    Text("Modes")
                }
            }
        }
    }
}
