package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.MotionPolicy
import com.vibecheck.app.ui.theme.VibeColors

data class SimulatedResponseUi(
    val personaName: String,
    val option: String,
    val isFictionalSimulation: Boolean,
)

@Composable
fun GameScreen(
    mode: GameMode,
    questionText: String,
    progress: Int,
    total: Int,
    answers: List<String>,
    onAnswer: (String) -> Unit,
    onExit: () -> Unit,
    animatorScale: Float = 1f,
    simulatedResponses: List<SimulatedResponseUi> = emptyList(),
    onContinueSimulation: (() -> Unit)? = null,
) {
    val fraction = if (total <= 0) 0f else progress.toFloat() / total
    val animated by animateFloatAsState(
        fraction.coerceIn(0f, 1f),
        tween(MotionPolicy.durationMillis(280, animatorScale)),
        label = "questionProgress",
    )
    val reveal = remember { Animatable(1f) }
    val accent = when (mode) {
        GameMode.WHO_OF_US -> VibeColors.Purple
        GameMode.MOST_LIKELY -> VibeColors.Orange
        GameMode.RED_GREEN -> VibeColors.Green
        GameMode.KNOWS_ME -> VibeColors.Blue
    }
    val size = when {
        questionText.length > 110 -> 23.sp
        questionText.length > 80 -> 25.sp
        questionText.length > 55 -> 27.sp
        else -> 30.sp
    }
    val line = when {
        questionText.length > 110 -> 29.sp
        questionText.length > 80 -> 31.sp
        questionText.length > 55 -> 33.sp
        else -> 36.sp
    }
    var answering by remember(progress, questionText) { mutableStateOf(false) }

    LaunchedEffect(progress) {
        reveal.snapTo(0f)
        reveal.animateTo(1f, tween(MotionPolicy.durationMillis(220, animatorScale)))
    }

    fun submit(answer: String) {
        if (answering) return
        answering = true
        onAnswer(answer)
    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(Modifier.size(9.dp).background(accent, CircleShape))
                        Text(
                            mode.title,
                            color = accent,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                        )
                    }
                    Text(
                        AppLocale.pick("Question $progress sur $total", "Question $progress of $total"),
                        color = VibeColors.TextSecondary,
                        fontSize = 13.sp,
                    )
                }
                TextButton(
                    onClick = onExit,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = VibeColors.TextSecondary),
                ) {
                    Text(AppLocale.pick("Quitter", "Exit"), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { animated },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = accent,
                trackColor = accent.copy(alpha = .14f),
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, accent.copy(alpha = .24f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(34.dp),
            modifier = Modifier.fillMaxWidth().graphicsLayer {
                alpha = reveal.value
                translationY = (1f - reveal.value) * 24f
            },
        ) {
            Column(
                Modifier.padding(horizontal = 22.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = .14f)),
                    shape = RoundedCornerShape(999.dp),
                ) {
                    Text(
                        AppLocale.pick("CHOISIS SANS TROP RÉFLÉCHIR", "DON’T OVERTHINK IT"),
                        color = VibeColors.TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    questionText,
                    color = VibeColors.TextPrimary,
                    fontSize = size,
                    lineHeight = line,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (simulatedResponses.isNotEmpty()) {
            LazyColumn(
                Modifier.fillMaxWidth().weight(1f).graphicsLayer { alpha = reveal.value },
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VibeColors.Rose.copy(alpha = .14f)),
                        border = BorderStroke(1.dp, VibeColors.Rose.copy(alpha = .28f)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            AppLocale.pick(
                                "Simulation fictive hors ligne : les réponses des personnalités publiques sont générées pour le jeu et ne représentent pas leurs opinions réelles.",
                                "Offline fictional simulation: public-figure answers are generated for the game and do not represent their real opinions.",
                            ),
                            color = VibeColors.TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(14.dp),
                        )
                    }
                }

                itemsIndexed(simulatedResponses, key = { i, r -> "$i:${r.personaName}" }) { _, response ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, VibeColors.Rose.copy(alpha = .25f)),
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(15.dp)) {
                            Text(
                                "${response.personaName} → ${response.option}",
                                color = VibeColors.TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                AppLocale.pick("Simulation fictive", "Fictional simulation"),
                                color = VibeColors.Rose,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                item {
                    VibeActionSurface(
                        onClick = { onContinueSimulation?.invoke() },
                        enabled = onContinueSimulation != null,
                        modifier = Modifier.fillMaxWidth(),
                        accent = accent,
                        containerColor = accent,
                        emphasized = true,
                        minHeight = 58.dp,
                    ) {
                        Box(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                AppLocale.pick("Voir le résultat global", "See overall result"),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Black,
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxWidth().weight(1f).graphicsLayer { alpha = reveal.value },
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(answers, key = { i, answer -> "$i:$answer" }) { index, answer ->
                    val optionAccent = if (mode == GameMode.RED_GREEN) {
                        if (index == 0) VibeColors.Green else VibeColors.Rose
                    } else {
                        accent
                    }
                    val optionBackground = optionAccent.copy(alpha = if (mode == GameMode.RED_GREEN) .12f else .18f)

                    VibeActionSurface(
                        onClick = { submit(answer) },
                        enabled = !answering,
                        modifier = Modifier.fillMaxWidth(),
                        accent = optionAccent,
                        containerColor = optionBackground,
                        emphasized = false,
                        minHeight = 62.dp,
                    ) {
                        Box(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 17.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                answer,
                                color = VibeColors.TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}
