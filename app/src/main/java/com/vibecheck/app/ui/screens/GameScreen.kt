package com.vibecheck.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(mode.title, color = Color(0xFFC9A7FF), fontWeight = FontWeight.Bold)
                    Text(progress.toString() + " / " + total, color = Color(0xFF8B8494), fontSize = 14.sp)
                }
                TextButton(onClick = onExit) {
                    Text("Quitter", color = Color(0xFFBEB7C9))
                }
            }
        }

        Text(
            questionText,
            color = Color.White,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Black
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            answers.forEach { answer ->
                Button(
                    onClick = { onAnswer(answer) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
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
