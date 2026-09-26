package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun SoloPartyScreen(
    personas: List<Persona>,
    selectedIds: List<String>,
    query: String,
    playerName: String,
    validationMessage: String,
    canStart: Boolean,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onPlayerNameChange: (String) -> Unit,
    onTogglePersona: (String) -> Unit,
    onAutoCompose: () -> Unit,
    onStart: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text(AppLocale.pick("← Retour", "← Back"), fontWeight = FontWeight.Bold)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = VibeColors.Purple.copy(alpha = .12f)),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text(
                    AppLocale.pick("\${selectedIds.size}/7 personnalités", "\${selectedIds.size}/7 public figures"),
                    color = VibeColors.TextSecondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            AppLocale.pick("Ta soirée solo", "Your solo party"),
            style = MaterialTheme.typography.headlineMedium,
            color = VibeColors.TextPrimary,
            fontWeight = FontWeight.Black,
        )
        Text(
            AppLocale.pick(
                "1. Tu réponds • 2. Les personnalités simulées répondent • 3. Résultat global",
                "1. You answer • 2. Simulated public figures answer • 3. Overall result",
            ),
            color = VibeColors.TextSecondary,
        )

        Spacer(Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = VibeColors.Green.copy(alpha = .16f)),
            border = BorderStroke(1.dp, VibeColors.Green.copy(alpha = .28f)),
            shape = RoundedCornerShape(26.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    AppLocale.pick("TOI • PARTICIPANT RÉEL", "YOU • REAL PLAYER"),
                    fontWeight = FontWeight.Black,
                    color = VibeColors.Green,
                )
                Spacer(Modifier.height(5.dp))
                OutlinedTextField(
                    value = playerName,
                    onValueChange = onPlayerNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(AppLocale.pick("Moi / mon pseudo", "Me / my nickname")) },
                    shape = RoundedCornerShape(18.dp),
                )
                Text(
                    AppLocale.pick(
                        "Ta réponse compte réellement dans le résultat de cette partie.",
                        "Your answer genuinely counts toward this game’s result.",
                    ),
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Spacer(Modifier.height(9.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = VibeColors.Rose.copy(alpha = .14f)),
            border = BorderStroke(1.dp, VibeColors.Rose.copy(alpha = .27f)),
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    AppLocale.pick("SIMULATION FICTIVE", "FICTIONAL SIMULATION"),
                    color = VibeColors.Rose,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    AppLocale.pick(
                        "Les réponses attribuées aux personnalités publiques sont simulées pour le divertissement. Elles ne viennent pas de ces personnes et ne représentent pas leurs opinions réelles.",
                        "Answers attributed to public figures are simulated for entertainment. They do not come from those people and do not represent their real opinions.",
                    ),
                    color = VibeColors.TextSecondary,
                )
            }
        }

        Spacer(Modifier.height(9.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(AppLocale.pick("Rechercher une personnalité", "Search public figures")) },
            shape = RoundedCornerShape(18.dp),
        )

        OutlinedButton(
            onClick = onAutoCompose,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, VibeColors.Purple.copy(alpha = .42f)),
        ) {
            Text(
                AppLocale.pick("Composer automatiquement", "Auto-compose group"),
                color = VibeColors.TextPrimary,
                fontWeight = FontWeight.Bold,
            )
        }

        if (validationMessage.isNotBlank()) {
            Text(validationMessage, color = VibeColors.Rose, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(personas, key = { it.id }) { persona ->
                val selected = persona.id in selectedIds
                Card(
                    onClick = { onTogglePersona(persona.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = persona.displayName +
                                if (selected) AppLocale.pick(", sélectionné", ", selected") else ""
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) {
                            VibeColors.Purple.copy(alpha = .16f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = BorderStroke(
                        if (selected) 2.dp else 1.dp,
                        if (selected) VibeColors.Purple.copy(alpha = .55f)
                        else MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 3.dp else 1.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                persona.displayName,
                                fontWeight = FontWeight.ExtraBold,
                                color = VibeColors.TextPrimary,
                            )
                            Text(persona.archetype, color = VibeColors.TextSecondary)
                        }
                        Text(
                            "SIMULATION",
                            color = VibeColors.Rose,
                            fontWeight = FontWeight.Black,
                        )
                    }
                }
            }
        }

        Button(
            onClick = onStart,
            enabled = canStart,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VibeColors.Purple,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 1.dp),
        ) {
            Text(AppLocale.pick("Je participe — démarrer", "I’m playing — start"), fontWeight = FontWeight.Black)
        }
    }
}
