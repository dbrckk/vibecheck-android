package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.PlayerRules
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun PlayerSetupScreen(
    mode: GameMode,
    players: List<String>,
    challengeTarget: Int?,
    onBack: () -> Unit,
    onAddPlayer: (String) -> Unit,
    onRemovePlayer: (String) -> Unit,
    onStart: () -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }
    val normalized = PlayerRules.normalize(input)
    val canAdd = PlayerRules.canAdd(players, input)
    val canStart = PlayerRules.canStart(players)
    val capacityProgress = players.size.toFloat() / PlayerRules.MAX_PLAYERS.toFloat()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
                Text(
                    "Crée ton groupe",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ajoute entre " + PlayerRules.MIN_PLAYERS + " et " + PlayerRules.MAX_PLAYERS + " joueurs.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    players.size.toString() + "/" + PlayerRules.MAX_PLAYERS,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { capacityProgress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
            )
            if (challengeTarget != null) {
                Spacer(Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (mode == GameMode.KNOWS_ME) {
                            "Défi reçu • score cible : " + challengeTarget + "%"
                        } else {
                            "Défi reçu • consensus cible : " + challengeTarget + "%"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { input = it.take(PlayerRules.MAX_NAME_LENGTH + 4) },
                label = { Text("Prénom ou pseudo") },
                supportingText = {
                    Text(
                        input.length.toString() + "/" + PlayerRules.MAX_NAME_LENGTH,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (canAdd) {
                            onAddPlayer(normalized)
                            input = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = Color.White.copy(alpha = 0.035f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.02f)
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {
                    onAddPlayer(normalized)
                    input = ""
                },
                enabled = canAdd && players.size < PlayerRules.MAX_PLAYERS,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    disabledContentColor = Color.White.copy(alpha = 0.35f)
                )
            ) {
                Text("Ajouter le joueur")
            }

            Spacer(Modifier.height(18.dp))

            if (players.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.07f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Ton groupe est vide",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Ajoute " + PlayerRules.MIN_PLAYERS + " joueurs minimum pour commencer.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(players, key = { it }) { player ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            player.take(1).uppercase(),
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                    Text(
                                        player,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                IconButton(onClick = { onRemovePlayer(player) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Supprimer " + player,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onStart,
                enabled = canStart,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = Color.White.copy(alpha = 0.34f)
                )
            ) {
                Text(
                    if (canStart) "Lancer la partie" else "Ajoute au moins " + PlayerRules.MIN_PLAYERS + " joueurs",
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.06f),
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Retour")
            }
        }
    }
}
