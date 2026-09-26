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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.ui.state.SoloPartyState
import com.vibecheck.app.ui.theme.VibeBackdrop

private enum class SoloCatalogTab { PUBLIC, ORIGINAL }

@Composable
fun SoloPartyScreen(
    personas: List<Persona>,
    state: SoloPartyState,
    onToggle: (String) -> Unit,
    onAutoCompose: () -> Unit,
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    var tab by remember { mutableStateOf(SoloCatalogTab.PUBLIC) }
    var query by remember { mutableStateOf("") }
    val visible = personas.filter {
        val kindMatches = if (tab == SoloCatalogTab.PUBLIC) it.kind == PersonaKind.PUBLIC_SIMULATION else it.kind == PersonaKind.ORIGINAL
        kindMatches && (query.isBlank() || it.displayName.contains(query, ignoreCase = true) || it.archetype.contains(query, ignoreCase = true))
    }

    VibeBackdrop {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Retour", Modifier.clickable(onClick = onBack).padding(8.dp), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = .14f)) {
                    Text("${state.selectedIds.size}/7", Modifier.padding(horizontal = 12.dp, vertical = 7.dp), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Ton casting", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
            Text("Choisis 2 à 7 compagnons pour jouer sans avoir besoin d'un groupe réel.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.tertiary.copy(alpha = .10f), border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = .24f))) {
                Text("Simulation fictive : les réponses des personnalités publiques sont inventées pour le jeu et ne représentent pas leurs opinions réelles.", Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton("Célébrités", tab == SoloCatalogTab.PUBLIC, { tab = SoloCatalogTab.PUBLIC }, Modifier.weight(1f))
                FilterButton("Originaux", tab == SoloCatalogTab.ORIGINAL, { tab = SoloCatalogTab.ORIGINAL }, Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Rechercher") })
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onAutoCompose, modifier = Modifier.fillMaxWidth()) { Text("Composer automatiquement") }
            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(visible, key = { it.id }) { persona ->
                    PersonaCard(persona, persona.id in state.selectedIds) { onToggle(persona.id) }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onStart, enabled = state.canStart, modifier = Modifier.fillMaxWidth()) {
                Text(if (state.canStart) "Commencer • ${state.selectedIds.size} compagnons" else "Commencer")
            }
        }
    }
}

@Composable
private fun FilterButton(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Card(modifier = modifier.clickable(onClick = onClick), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = .18f) else MaterialTheme.colorScheme.surface), border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .16f))) {
        Text(label, Modifier.padding(vertical = 10.dp).fillMaxWidth(), color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
private fun PersonaCard(persona: Persona, selected: Boolean, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = .12f) else MaterialTheme.colorScheme.surface), border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .14f))) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondary.copy(alpha = .16f)) {
                Text(persona.displayName.take(1).uppercase(), Modifier.padding(horizontal = 15.dp, vertical = 11.dp), color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.titleLarge)
            }
            Column(Modifier.weight(1f)) {
                Text(persona.displayName, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Text(persona.archetype, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                Text(persona.description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
            if (selected) Text("✓", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
        }
    }
}
