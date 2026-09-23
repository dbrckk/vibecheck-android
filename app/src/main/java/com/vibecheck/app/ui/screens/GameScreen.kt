package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
    val haptic = LocalHapticFeedback.current
    var answering by remember(questionText) { mutableStateOf(false) }

    LaunchedEffect(questionText) {
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
                    Text(
                        mode.title,
                        color = Color(0xFFC9A7FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        "Question " + progress + " sur " + total,
                        color = Color(0xFF8B8494),
                        fontSize = 13.sp
                    )
                }
                TextButton(onClick = onExit) {
                    Text("Quitter", color = Color(0xFFBEB7C9))
                }
            }

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFFC9A7FF),
                trackColor = Color(0xFF302A39)
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xE6211E29)),
            shape = RoundedCornerShape(28.dp),
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
                Text(
                    "CHOISIS SANS TROP RÉFLÉCHIR",
                    color = Color(0xFF9E91AE),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    questionText,
                    color = Color.White,
                    fontSize = 29.sp,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.graphicsLayer {
                alpha = reveal.value
                translationY = (1f - reveal.value) * 32f
            },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            answers.forEachIndexed { index, answer ->
                if (mode == GameMode.RED_GREEN) {
                    OutlinedButton(
                        onClick = { submit(answer) },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            1.dp,
                            if (index == 0) Color(0xFF9BE6C1) else Color(0xFFFFA3B1)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = Color(0x33211E29)
                        )
                    ) {
                        Text(answer, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { submit(answer) },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEE6FF),
                            contentColor = Color(0xFF18121F)
                        )
                    ) {
                        Text(answer, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
