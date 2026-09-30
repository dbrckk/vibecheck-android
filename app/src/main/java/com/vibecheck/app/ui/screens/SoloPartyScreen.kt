package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun SoloPartyScreen(
    personas: List<Persona>,
    selectedIds: List<String>,
    selectedMode: GameMode,
    query: String,
    playerName: String,
    validationMessage: String,
    canStart: Boolean,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onPlayerNameChange: (String) -> Unit,
    onModeChange: (GameMode) -> Unit,
    onTogglePersona: (String) -> Unit,
    onAutoCompose: () -> Unit,
    onStart: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledTonalIconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, AppLocale.pick("Retour", "Back"))
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = VibeColors.Purple.copy(alpha = .14f)),
                border = BorderStroke(1.dp, VibeColors.Purple.copy(alpha = .28f)),
                shape = RoundedCornerShape(999.dp),
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Icon(Icons.Default.Groups, null, tint = VibeColors.Purple, modifier = Modifier.size(18.dp))
                    Text(
                        selectedIds.size.toString() + "/7 " + AppLocale.pick("compagnons", "companions"),
                        color = VibeColors.TextPrimary,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    AppLocale.pick("Ta soirée solo", "Your solo party"),
                    style = MaterialTheme.typography.headlineLarge,
                    color = VibeColors.TextPrimary,
                    fontWeight = FontWeight.Black,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    AppLocale.pick(
                        "Tu réponds d’abord. Ton groupe simulé répond ensuite. Puis on compare.",
                        "You answer first. Your simulated group responds next. Then we compare.",
                    ),
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            item {
                SoloSteps()
            }

            item {
                SectionTitle(AppLocale.pick("MODE DE JEU", "GAME MODE"))
                Spacer(Modifier.height(7.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    listOf(GameMode.WHO_OF_US, GameMode.MOST_LIKELY, GameMode.RED_GREEN).forEach { mode ->
                        val selected = mode == selectedMode
                        val accent = when (mode) {
                            GameMode.WHO_OF_US -> VibeColors.Purple
                            GameMode.MOST_LIKELY -> VibeColors.Orange
                            GameMode.RED_GREEN -> VibeColors.Green
                            GameMode.KNOWS_ME -> VibeColors.Blue
                        }
                        VibeActionSurface(
                            onClick = { onModeChange(mode) },
                            modifier = Modifier.weight(1f).semantics { this.selected = selected },
                            accent = accent,
                            containerColor = if (selected) accent.copy(alpha = .22f)
                                else MaterialTheme.colorScheme.surface,
                            emphasized = selected,
                            minHeight = 58.dp,
                        ) {
                            Box(
                                Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    mode.title,
                                    color = VibeColors.TextPrimary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black,
                                    maxLines = 2,
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = VibeColors.Green.copy(alpha = .16f)),
                    border = BorderStroke(1.dp, VibeColors.Green.copy(alpha = .31f)),
                    shape = RoundedCornerShape(26.dp),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            AppLocale.pick("TOI • PARTICIPANT RÉEL", "YOU • REAL PLAYER"),
                            fontWeight = FontWeight.Black,
                            color = VibeColors.Green,
                            fontSize = 12.sp,
                            letterSpacing = .5.sp,
                        )
                        Spacer(Modifier.height(7.dp))
                        OutlinedTextField(
                            value = playerName,
                            onValueChange = onPlayerNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text(AppLocale.pick("Moi / mon pseudo", "Me / my nickname")) },
                            shape = RoundedCornerShape(18.dp),
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            AppLocale.pick(
                                "Ta réponse est réelle et compte dans le résultat.",
                                "Your answer is real and counts toward the result.",
                            ),
                            color = VibeColors.TextSecondary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = VibeColors.Rose.copy(alpha = .13f)),
                    border = BorderStroke(1.dp, VibeColors.Rose.copy(alpha = .29f)),
                    shape = RoundedCornerShape(22.dp),
                ) {
                    Column(Modifier.padding(15.dp)) {
                        Text(
                            AppLocale.pick("SIMULATION FICTIVE", "FICTIONAL SIMULATION"),
                            color = VibeColors.Rose,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = .45.sp,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            AppLocale.pick(
                                "Les réponses attribuées aux personnalités publiques sont générées pour le divertissement. Elles ne viennent pas de ces personnes et ne représentent pas leurs opinions.",
                                "Answers attributed to public figures are generated for entertainment. They do not come from those people and do not represent their opinions.",
                            ),
                            color = VibeColors.TextSecondary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            item {
                SectionTitle(AppLocale.pick("TON GROUPE", "YOUR GROUP"))
                Spacer(Modifier.height(7.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    label = { Text(AppLocale.pick("Rechercher une personnalité", "Search public figures")) },
                    shape = RoundedCornerShape(20.dp),
                )
                Spacer(Modifier.height(9.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VibeColors.Green.copy(alpha = .13f)),
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Text(
                            AppLocale.pick("Publics uniquement", "Public only"),
                            color = VibeColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        )
                    }
                    Text(
                        selectedIds.size.toString() + " / 7",
                        color = VibeColors.Purple,
                        fontWeight = FontWeight.Black,
                    )
                }
            }

            item {
                VibeActionSurface(
                    onClick = onAutoCompose,
                    modifier = Modifier.fillMaxWidth(),
                    accent = VibeColors.Purple,
                    containerColor = VibeColors.Purple.copy(alpha = .14f),
                    emphasized = true,
                    minHeight = 58.dp,
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, tint = VibeColors.Purple)
                            Text(
                                AppLocale.pick("Composer automatiquement", "Auto-compose group"),
                                color = VibeColors.TextPrimary,
                                fontWeight = FontWeight.Black,
                            )
                        }
                        Icon(Icons.Default.ArrowForward, null, tint = VibeColors.Purple)
                    }
                }
            }

            if (validationMessage.isNotBlank()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VibeColors.Rose.copy(alpha = .10f)),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            validationMessage,
                            color = VibeColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                        )
                    }
                }
            }

            items(personas, key = { it.id }) { persona ->
                val selected = persona.id in selectedIds
                PersonaCard(
                    persona = persona,
                    selected = selected,
                    onClick = { onTogglePersona(persona.id) },
                )
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.background.copy(alpha = .97f),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .navigationBarsPadding(),
            ) {
                VibeActionSurface(
                    onClick = onStart,
                    enabled = canStart,
                    modifier = Modifier.fillMaxWidth(),
                    accent = VibeColors.Purple,
                    containerColor = VibeColors.Purple,
                    emphasized = true,
                    minHeight = 62.dp,
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 17.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            if (canStart) {
                                AppLocale.pick("Je participe — démarrer", "I’m playing — start")
                            } else {
                                AppLocale.pick("Choisis au moins 2 compagnons", "Choose at least 2 companions")
                            },
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Black,
                        )
                        if (canStart) {
                            Spacer(Modifier.width(9.dp))
                            Icon(Icons.Default.ArrowForward, null, tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SoloSteps() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .76f)),
        border = BorderStroke(1.dp, VibeColors.Purple.copy(alpha = .18f)),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Step("1", AppLocale.pick("Tu réponds", "You answer"), VibeColors.Rose)
            Text("→", color = VibeColors.TextSecondary, modifier = Modifier.padding(top = 8.dp))
            Step("2", AppLocale.pick("Ils répondent", "They answer"), VibeColors.Purple)
            Text("→", color = VibeColors.TextSecondary, modifier = Modifier.padding(top = 8.dp))
            Step("3", AppLocale.pick("On compare", "Compare"), VibeColors.Green)
        }
    }
}

@Composable
private fun RowScope.Step(number: String, label: String, accent: androidx.compose.ui.graphics.Color) {
    Column(
        Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(32.dp).background(accent.copy(alpha = .25f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(number, color = VibeColors.TextPrimary, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(5.dp))
        Text(
            label,
            color = VibeColors.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            maxLines = 2,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = VibeColors.TextSecondary,
        fontWeight = FontWeight.Black,
        fontSize = 11.sp,
        letterSpacing = 1.1.sp,
    )
}

@Composable
private fun PersonaCard(persona: Persona, selected: Boolean, onClick: () -> Unit) {
    VibeActionSurface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                this.selected = selected
                contentDescription = persona.displayName +
                    if (selected) AppLocale.pick(", sélectionné", ", selected") else ""
            },
        accent = VibeColors.Purple,
        containerColor = if (selected) VibeColors.Purple.copy(alpha = .17f)
            else MaterialTheme.colorScheme.surface,
        emphasized = selected,
        minHeight = 78.dp,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier.size(42.dp).background(
                    if (selected) VibeColors.Purple.copy(alpha = .24f) else VibeColors.Blue.copy(alpha = .18f),
                    CircleShape,
                ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    persona.displayName.take(1).uppercase(),
                    color = VibeColors.TextPrimary,
                    fontWeight = FontWeight.Black,
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    persona.displayName,
                    fontWeight = FontWeight.ExtraBold,
                    color = VibeColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    persona.archetype,
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) VibeColors.Purple.copy(alpha = .22f)
                    else VibeColors.Rose.copy(alpha = .11f),
                ),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text(
                    if (selected) AppLocale.pick("✓ SÉLECTIONNÉE", "✓ SELECTED") else AppLocale.pick("AJOUTER", "ADD"),
                    color = VibeColors.TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                )
            }
        }
    }
}
