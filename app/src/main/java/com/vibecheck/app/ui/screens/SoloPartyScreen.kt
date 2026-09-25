package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun SoloPartyScreen(
    personas: List<Persona>,
    selectedIds: List<String>,
    selectedKind: PersonaKind?,
    query: String,
    validationMessage: String,
    canStart: Boolean,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onKindChange: (PersonaKind?) -> Unit,
    onTogglePersona: (String) -> Unit,
    onAutoCompose: () -> Unit,
    onStart: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VibeColors.SurfaceStrong,
                    contentColor = VibeColors.TextPrimary,
                ),
            ) {
                Text("Retour")
            }
            Text(
                text = selectedIds.size.toString() + "/7",
                color = VibeColors.TextSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        Spacer(Modifier.height(14.dp))
        Text(
            text = "Groupe solo",
            color = VibeColors.TextPrimary,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Choisis 2 à 7 compagnons. Les profils publics utilisent une Simulation fictive pour le divertissement.",
            color = VibeColors.TextSecondary,
        )

        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Rechercher un profil") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = VibeColors.TextPrimary,
                unfocusedTextColor = VibeColors.TextPrimary,
                focusedBorderColor = VibeColors.Purple,
                unfocusedBorderColor = VibeColors.SurfaceStrong,
                focusedLabelColor = VibeColors.Purple,
                unfocusedLabelColor = VibeColors.TextSecondary,
            ),
        )

        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedKind == null,
                onClick = { onKindChange(null) },
                label = { Text("Tous") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibeColors.Purple.copy(alpha = 0.24f),
                    selectedLabelColor = VibeColors.TextPrimary,
                    labelColor = VibeColors.TextSecondary,
                ),
            )
            FilterChip(
                selected = selectedKind == PersonaKind.PUBLIC_SIMULATION,
                onClick = { onKindChange(PersonaKind.PUBLIC_SIMULATION) },
                label = { Text("Public") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibeColors.Purple.copy(alpha = 0.24f),
                    selectedLabelColor = VibeColors.TextPrimary,
                    labelColor = VibeColors.TextSecondary,
                ),
            )
            FilterChip(
                selected = selectedKind == PersonaKind.ORIGINAL,
                onClick = { onKindChange(PersonaKind.ORIGINAL) },
                label = { Text("Originaux") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibeColors.Purple.copy(alpha = 0.24f),
                    selectedLabelColor = VibeColors.TextPrimary,
                    labelColor = VibeColors.TextSecondary,
                ),
            )
        }

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = onAutoCompose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VibeColors.SurfaceStrong,
                contentColor = VibeColors.TextPrimary,
            ),
        ) {
            Text("Composer automatiquement")
        }

        if (validationMessage.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = validationMessage,
                color = VibeColors.Orange,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(personas, key = { it.id }) { persona ->
                val selected = persona.id in selectedIds
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .semantics {
                            contentDescription = if (selected) {
                                persona.displayName + ", sélectionné"
                            } else {
                                persona.displayName
                            }
                        }
                        .clickable { onTogglePersona(persona.id) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) {
                            VibeColors.Purple.copy(alpha = 0.18f)
                        } else {
                            VibeColors.Surface.copy(alpha = 0.92f)
                        },
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selected) VibeColors.Purple else VibeColors.SurfaceStrong,
                    ),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = persona.displayName,
                            color = VibeColors.TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        Text(
                            text = persona.archetype,
                            color = VibeColors.TextSecondary,
                        )
                        if (persona.isFictionalSimulation) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Simulation fictive",
                                color = VibeColors.Purple,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = onStart,
            enabled = canStart,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Démarrer la partie")
        }
    }
}
