package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.*
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun HomeScreen(
    selectedIntensity: GameIntensity,
    selectedPack: GamePack,
    savedPlayerCount: Int,
    groupLeaderName: String?,
    groupLeaderWins: Int,
    onOpenLeaderboard: () -> Unit,
    onEditGroup: () -> Unit,
    onOpenSettings: () -> Unit,
    onPlaySolo: () -> Unit,
    onIntensity: (GameIntensity) -> Unit,
    onPack: (GamePack) -> Unit,
    onMode: (GameMode) -> Unit,
) {
    var options by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Box(Modifier.size(10.dp).background(VibeColors.Green, CircleShape))
                    Text(
                        "VIBECHECK",
                        color = VibeColors.Purple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.2.sp,
                    )
                }
                FilledTonalIconButton(onClick = onOpenSettings) {
                    Icon(
                        Icons.Default.Settings,
                        AppLocale.pick("Ouvrir les réglages", "Open settings"),
                        tint = VibeColors.TextPrimary,
                    )
                }
            }
        }

        item {
            Text(
                AppLocale.pick("Qui vous connaît vraiment ?", "Who really knows you?"),
                color = VibeColors.TextPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                AppLocale.pick(
                    "Répondez, comparez vos choix et découvrez votre vibe.",
                    "Answer, compare your choices and discover your vibe.",
                ),
                color = VibeColors.TextSecondary,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        item {
            PlayEntryCard(
                eyebrow = AppLocale.pick("À PLUSIEURS", "TOGETHER"),
                title = AppLocale.pick("Jouer ensemble", "Play together"),
                subtitle = if (savedPlayerCount >= 2) {
                    AppLocale.pick(
                        savedPlayerCount.toString() + " joueurs mémorisés • reprise rapide",
                        savedPlayerCount.toString() + " saved players • quick resume",
                    )
                } else {
                    AppLocale.pick(
                        "Passez le téléphone et comparez vos réponses.",
                        "Pass the phone and compare your answers.",
                    )
                },
                cta = AppLocale.pick("Choisir un mode", "Choose a mode"),
                icon = Icons.Default.Groups,
                accent = VibeColors.Purple,
                emphasized = true,
                onClick = { options = true },
            )
        }

        item {
            PlayEntryCard(
                eyebrow = AppLocale.pick("EN SOLO", "SOLO"),
                title = AppLocale.pick("Jouer en solo", "Play solo"),
                subtitle = AppLocale.pick(
                    "Vous jouez d’abord, puis des personnalités publiques simulées répondent.",
                    "You play first, then simulated public figures respond.",
                ),
                cta = AppLocale.pick("Créer mon groupe solo", "Build my solo group"),
                icon = Icons.Default.Psychology,
                accent = VibeColors.Rose,
                emphasized = false,
                onClick = onPlaySolo,
            )
        }

        if (savedPlayerCount >= 2) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = VibeColors.Green.copy(alpha = .15f)),
                    border = BorderStroke(1.dp, VibeColors.Green.copy(alpha = .34f)),
                    shape = RoundedCornerShape(22.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = VibeColors.Green)
                        Column(Modifier.weight(1f)) {
                            Text(
                                AppLocale.pick("Groupe prêt", "Group ready"),
                                color = VibeColors.TextPrimary,
                                fontWeight = FontWeight.Black,
                            )
                            Text(
                                AppLocale.pick(
                                    savedPlayerCount.toString() + " joueurs mémorisés",
                                    savedPlayerCount.toString() + " saved players",
                                ),
                                color = VibeColors.TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        TextButton(onClick = onEditGroup) {
                            Text(AppLocale.pick("Modifier", "Edit"), fontWeight = FontWeight.Black)
                        }
                    }
                }

                if (groupLeaderName != null && groupLeaderWins > 0) {
                    Row(
                        Modifier.fillMaxWidth().padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "Leader : " + groupLeaderName + " • " + groupLeaderWins + " " +
                                AppLocale.pick(
                                    if (groupLeaderWins == 1) "victoire" else "victoires",
                                    if (groupLeaderWins == 1) "win" else "wins",
                                ),
                            color = VibeColors.TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        TextButton(onClick = onOpenLeaderboard) {
                            Text(AppLocale.pick("Classement", "Leaderboard"))
                        }
                    }
                }
            }
        }

        if (options) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .92f)),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, VibeColors.Purple.copy(alpha = .24f)),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        SectionLabel(AppLocale.pick("PACK", "PACK"))
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            GamePack.entries.forEach { p ->
                                ChoiceChip(p.title, p == selectedPack, VibeColors.Blue) { onPack(p) }
                            }
                        }
                        Spacer(Modifier.height(7.dp))
                        Text(selectedPack.subtitle, color = VibeColors.TextSecondary, fontSize = 12.sp)

                        Spacer(Modifier.height(16.dp))
                        SectionLabel(AppLocale.pick("INTENSITÉ", "INTENSITY"))
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            GameIntensity.entries.forEach { intensity ->
                                ChoiceChip(
                                    intensity.title,
                                    intensity == selectedIntensity,
                                    VibeColors.Purple,
                                ) { onIntensity(intensity) }
                            }
                        }
                        Spacer(Modifier.height(7.dp))
                        Text(selectedIntensity.subtitle, color = VibeColors.TextSecondary, fontSize = 12.sp)
                    }
                }
            }

            item {
                SectionLabel(AppLocale.pick("CHOISIS TON VIBE", "CHOOSE YOUR VIBE"))
            }

            items(GameMode.entries.size) { index ->
                val mode = GameMode.entries[index]
                ModeCard(mode, savedPlayerCount >= 2 || mode == GameMode.RED_GREEN) { onMode(mode) }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) = Text(
    text,
    color = VibeColors.TextSecondary,
    fontSize = 11.sp,
    fontWeight = FontWeight.Black,
    letterSpacing = 1.2.sp,
)

@Composable
private fun RowScope.ChoiceChip(
    label: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
) {
    VibeActionSurface(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        accent = accent,
        containerColor = if (selected) accent.copy(alpha = .24f) else MaterialTheme.colorScheme.surface,
        emphasized = selected,
        minHeight = 48.dp,
    ) {
        Box(
            Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                label,
                color = VibeColors.TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun PlayEntryCard(
    eyebrow: String,
    title: String,
    subtitle: String,
    cta: String,
    icon: ImageVector,
    accent: Color,
    emphasized: Boolean,
    onClick: () -> Unit,
) {
    VibeActionSurface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        accent = accent,
        containerColor = if (emphasized) accent.copy(alpha = .16f) else MaterialTheme.colorScheme.surface,
        emphasized = emphasized,
        minHeight = 164.dp,
    ) {
        Column(
            Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                Box(
                    Modifier.size(48.dp).background(accent.copy(alpha = .18f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = accent)
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        eyebrow,
                        color = accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.1.sp,
                    )
                    Text(
                        title,
                        color = VibeColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        subtitle,
                        color = VibeColors.TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(accent.copy(alpha = if (emphasized) .24f else .14f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    cta,
                    color = VibeColors.TextPrimary,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.labelLarge,
                )
                Icon(Icons.Default.ArrowForward, null, tint = accent)
            }
        }
    }
}

@Composable
private fun ModeCard(mode: GameMode, express: Boolean, onClick: () -> Unit) {
    val visual = modeVisual(mode)
    VibeActionSurface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        accent = visual.accent,
        containerColor = visual.accent.copy(alpha = .12f),
        minHeight = 92.dp,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                Modifier.size(48.dp).background(visual.accent.copy(alpha = .18f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(visual.icon, null, tint = visual.accent)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    mode.title,
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    if (express) AppLocale.pick("8 QUESTIONS • DÉMARRAGE DIRECT", "8 QUESTIONS • QUICK START")
                    else "8 QUESTIONS",
                    color = visual.accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    mode.subtitle,
                    color = VibeColors.TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(Icons.Default.ArrowForward, null, tint = visual.accent)
        }
    }
}

private data class ModeVisual(val icon: ImageVector, val accent: Color)

private fun modeVisual(mode: GameMode) = when (mode) {
    GameMode.WHO_OF_US -> ModeVisual(Icons.Default.Groups, VibeColors.Purple)
    GameMode.MOST_LIKELY -> ModeVisual(Icons.Default.Whatshot, VibeColors.Orange)
    GameMode.RED_GREEN -> ModeVisual(Icons.Default.Bolt, VibeColors.Green)
    GameMode.KNOWS_ME -> ModeVisual(Icons.Default.Psychology, VibeColors.Blue)
}
