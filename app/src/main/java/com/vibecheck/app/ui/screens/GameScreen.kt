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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun GameScreen(
    mode: GameMode,
    questionText: String,
    progress: Int,
    total: Int,
    answers: List<String>,
    onAnswer: (String) -> Unit,
    onExit: () -> Unit
) {
    val progressFraction = if (total <= 0) 0f else progress.toFloat() / total.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 280),
        label = "questionProgress"
    )
    val reveal = remember { Animatable(1f) }
    val accent = when (mode) {
        GameMode.WHO_OF_US -> VibeColors.Purple
        GameMode.MOST_LIKELY -> VibeColors.Orange
        GameMode.RED_GREEN -> VibeColors.Green
        GameMode.KNOWS_ME -> VibeColors.Blue
    }
    val questionFontSize = when {
        questionText.length > 110 -> 23.sp
        questionText.length > 80 -> 25.sp
        questionText.length > 55 -> 27.sp
        else -> 30.sp
    }
    val questionLineHeight = when {
        questionText.length > 110 -> 29.sp
        questionText.length > 80 -> 31.sp
        questionText.length > 55 -> 33.sp
        else -> 36.sp
    }
    val haptic = LocalHapticFeedback.current
    var answering by remember(progress, questionText) { mutableStateOf(false) }

    LaunchedEffect(progress) {
        reveal.snapTo(0f)
        reveal.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 220)
        )
    }

    fun submit(answer: String) {
        if (answering) return
        answering = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onAnswer(answer)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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
                            mode.title,
                            color = accent,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        "Question " + progress + " sur " + total,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                TextButton(
                    onClick = onExit,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        "Quitter",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = accent,
                trackColor = accent.copy(alpha = 0.14f)
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, accent.copy(alpha = 0.28f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = reveal.value
                    translationY = (1f - reveal.value) * 24f
                    scaleX = 0.985f + reveal.value * 0.015f
                    scaleY = 0.985f + reveal.value * 0.015f
                }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(accent.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        "CHOISIS SANS TROP RÉFLÉCHIR",
                        color = accent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.05.sp
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    questionText,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = questionFontSize,
                    lineHeight = questionLineHeight,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer {
                    alpha = reveal.value
                    translationY = (1f - reveal.value) * 32f
                },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(answers, key = { index, answer -> index.toString() + ":" + answer }) { index, answer ->
                val interactionSource = remember(answer, progress) { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val answerScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.985f else 1f,
                    animationSpec = tween(durationMillis = if (isPressed) 80 else 130),
                    label = "answerScale"
                )

                if (mode == GameMode.RED_GREEN) {
                    OutlinedButton(
                        onClick = { submit(answer) },
                        enabled = !answering,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp)
                            .graphicsLayer {
                                scaleX = answerScale
                                scaleY = answerScale
                            },
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            1.5.dp,
                            if (index == 0) VibeColors.Green else VibeColors.Rose
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = if (index == 0) VibeColors.Green.copy(alpha = 0.08f) else VibeColors.Rose.copy(alpha = 0.08f)
                        )
                    ) {
                        Text(answer, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { submit(answer) },
                        enabled = !answering,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp)
                            .graphicsLayer {
                                scaleX = answerScale
                                scaleY = answerScale
                            },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accent.copy(alpha = 0.16f),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = accent.copy(alpha = 0.08f),
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(answer, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
