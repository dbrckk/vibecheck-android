package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.PlayerStat
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun LeaderboardScreen(stats: List<PlayerStat>, onBack: () -> Unit) {
    val ranked = stats.sortedWith(
        compareByDescending<PlayerStat> { it.wins }
            .thenByDescending { it.bestScorePercent }
            .thenBy { it.name.lowercase() }
    )

    Column(Modifier.fillMaxSize()) {
        Text(
            AppLocale.pick("Classement du groupe", "Group leaderboard"),
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            AppLocale.pick(
                "Basé uniquement sur les parties jouées sur cet appareil.",
                "Based only on games played on this device.",
            ),
            color = VibeColors.TextSecondary,
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(14.dp))

        if (ranked.isEmpty() || ranked.all { it.wins == 0 }) {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = VibeColors.Orange.copy(alpha = .12f)),
                border = BorderStroke(1.dp, VibeColors.Orange.copy(alpha = .24f)),
            ) {
                Text(
                    AppLocale.pick(
                        "Joue quelques parties pour créer le classement.",
                        "Play a few games to build the leaderboard.",
                    ),
                    color = VibeColors.TextSecondary,
                    modifier = Modifier.padding(20.dp),
                )
            }
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
            ) {
                itemsIndexed(ranked, key = { _, stat -> stat.name }) { index, stat ->
                    val leader = index == 0 && stat.wins > 0
                    Card(
                        Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (leader) VibeColors.Purple.copy(alpha = .16f)
                            else MaterialTheme.colorScheme.surface,
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (leader) VibeColors.Purple.copy(alpha = .34f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Text(
                                "#${index + 1}",
                                color = if (leader) VibeColors.Purple else VibeColors.TextSecondary,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                            )
                            Column(Modifier.weight(1f)) {
                                Text(
                                    stat.name,
                                    color = VibeColors.TextPrimary,
                                    fontWeight = FontWeight.Black,
                                )
                                Text(
                                    AppLocale.pick(
                                        "${stat.wins} victoire${if (stat.wins == 1) "" else "s"}",
                                        "${stat.wins} win${if (stat.wins == 1) "" else "s"}",
                                    ),
                                    color = VibeColors.TextSecondary,
                                    fontSize = 12.sp,
                                )
                            }
                            Text(
                                "${stat.bestScorePercent}%",
                                color = VibeColors.Green,
                                fontWeight = FontWeight.Black,
                            )
                        }
                    }
                }
            }
        }

        VibeActionSurface(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            accent = VibeColors.Purple,
            containerColor = MaterialTheme.colorScheme.surface,
            minHeight = 56.dp,
        ) {
            Box(
                Modifier.fillMaxWidth().padding(15.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    AppLocale.pick("Retour", "Back"),
                    color = VibeColors.TextPrimary,
                    fontWeight = FontWeight.Black,
                )
            }
        }
    }
}
