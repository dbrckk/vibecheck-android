package com.vibecheck.app.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.PlayerStat
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun LeaderboardScreen(
    stats: List<PlayerStat>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Classement du groupe",
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Text(
            "Basé uniquement sur les parties jouées sur cet appareil.",
            color = VibeColors.TextSecondary,
            fontSize = 13.sp
        )

        if (stats.isEmpty() || stats.all { it.wins == 0 }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xF21B1721)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Text(
                    "Joue quelques parties pour créer le classement.",
                    color = VibeColors.TextSecondary,
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            stats.forEachIndexed { index, stat ->
                val leader = index == 0 && stat.wins > 0
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (leader) VibeColors.Purple.copy(alpha = 0.15f) else Color(0xF21B1721)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (leader) VibeColors.Purple.copy(alpha = 0.40f) else Color.White.copy(alpha = 0.07f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "#" + (index + 1),
                            color = if (leader) VibeColors.PurpleBright else VibeColors.TextSecondary,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stat.name, color = VibeColors.TextPrimary, fontWeight = FontWeight.Black)
                            Text(
                                stat.wins.toString() + if (stat.wins == 1) " victoire" else " victoires",
                                color = VibeColors.TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            stat.bestScorePercent.toString() + "%",
                            color = VibeColors.Green,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF302A39),
                contentColor = VibeColors.TextPrimary
            )
        ) {
            Text("Retour", fontWeight = FontWeight.Bold)
        }
    }
}