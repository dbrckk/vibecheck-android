package com.vibecheck.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun ResultScreen(
    mode: GameMode,
    votes: List<Vote>,
    challengeTarget: Int?,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val result = GameEngine.result(votes)
    val context = LocalContext.current
    val percent = if (result.total == 0) 0 else (result.score * 100 / result.total)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("VIBECHECK", color = Color(0xFFC9A7FF), fontWeight = FontWeight.Black)
            Spacer(Modifier.height(30.dp))
            Text(
                result.winner,
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Text(
                percent.toString() + "%",
                color = Color(0xFFE8D8FF),
                fontSize = 72.sp,
                fontWeight = FontWeight.Black
            )
            Text("sur " + mode.title, color = Color(0xFFA9A3B3), textAlign = TextAlign.Center)
            if (challengeTarget != null) {
                Spacer(Modifier.height(14.dp))
                Text(
                    if (percent > challengeTarget) {
                        "Défi réussi • " + challengeTarget + "% à battre"
                    } else {
                        "Défi à battre • objectif " + challengeTarget + "%"
                    },
                    color = if (percent > challengeTarget) Color(0xFFC9A7FF) else Color(0xFFBEB7C9),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    ResultCardSharer.share(
                        context = context,
                        mode = mode,
                        winner = result.winner,
                        percent = percent
                    )
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Text("Partager le résultat", fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = {
                    ChallengeSharer.share(
                        context = context,
                        challenge = Challenge(mode = mode, targetPercent = percent)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B2E6B))
            ) {
                Text("Défier un ami", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onReplay,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Rejouer")
            }

            Button(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF302A39))
            ) {
                Text("Changer de mode")
            }
        }
    }
}
