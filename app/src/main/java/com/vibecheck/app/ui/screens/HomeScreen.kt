package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.billing.PurchaseStatus
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun HomeScreen(
    isPremium: Boolean,
    premiumReady: Boolean,
    premiumPrice: String?,
    purchaseStatus: PurchaseStatus,
    selectedIntensity: GameIntensity,
    selectedPack: GamePack,
    savedPlayerCount: Int,
    groupLeaderName: String?,
    groupLeaderWins: Int,
    onOpenLeaderboard: () -> Unit,
    onEditGroup: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onBuyPremium: () -> Unit,
    onIntensity: (GameIntensity) -> Unit,
    onPack: (GamePack) -> Unit,
    onMode: (GameMode) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                )
                Text(
                    "VIBECHECK",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.2.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .clickable(onClick = onOpenSettings),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Paramètres",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(21.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Qui connaît vraiment qui ?",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "8 questions. Un groupe. Zéro filtre.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )

        if (savedPlayerCount >= 2) {
            Spacer(Modifier.height(10.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.28f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$savedPlayerCount joueurs mémorisés • groupe prêt",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Modifier",
                        modifier = Modifier.clickable(onClick = onEditGroup),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if (groupLeaderName != null && groupLeaderWins > 0) {
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenLeaderboard),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Leader du groupe",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "$groupLeaderName • $groupLeaderWins victoire${if (groupLeaderWins > 1) "s" else ""}",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Text("Classement", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(modeCards) { card ->
                ModeCard(card = card, onClick = { onMode(card.mode) })
            }
            item {
                Spacer(Modifier.height(4.dp))
                Text("Ambiance", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GameIntensity.entries.forEach { intensity ->
                        CompactChoice(
                            label = intensityLabel(intensity),
                            selected = intensity == selectedIntensity,
                            onClick = { onIntensity(intensity) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
                Text("Pack", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GamePack.entries.forEach { pack ->
                        CompactChoice(
                            label = packLabel(pack),
                            selected = pack == selectedPack,
                            onClick = { onPack(pack) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(6.dp))
                PremiumCard(
                    isPremium = isPremium,
                    premiumReady = premiumReady,
                    premiumPrice = premiumPrice,
                    purchaseStatus = purchaseStatus,
                    onBuyPremium = onBuyPremium
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

private data class ModeCardModel(
    val mode: GameMode,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color
)

private val modeCards = listOf(
    ModeCardModel(GameMode.WHO_OF_US, "Who of Us", "Votez. Le groupe tranche.", Icons.Filled.Groups, VibeColors.Purple),
    ModeCardModel(GameMode.MOST_LIKELY, "Most Likely", "Qui ferait vraiment ça ?", Icons.Filled.Bolt, VibeColors.Blue),
    ModeCardModel(GameMode.RED_GREEN, "Red / Green", "Red flag ou green flag ?", Icons.Filled.Whatshot, VibeColors.Orange),
    ModeCardModel(GameMode.KNOWS_ME, "Knows Me", "Qui te connaît le mieux ?", Icons.Filled.Psychology, VibeColors.Green)
)

@Composable
private fun ModeCard(card: ModeCardModel, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = tween(100),
        label = "modePress"
    )
    Card(
        onClick = onClick,
        interactionSource = interaction,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, card.accent.copy(alpha = 0.20f)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(card.accent.copy(alpha = 0.14f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(card.icon, contentDescription = null, tint = card.accent)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(card.title, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Text(card.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Text("→", color = card.accent, fontWeight = FontWeight.Black, fontSize = 20.sp)
        }
    }
}

@Composable
private fun CompactChoice(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1)
    }
}

@Composable
private fun PremiumCard(
    isPremium: Boolean,
    premiumReady: Boolean,
    premiumPrice: String?,
    purchaseStatus: PurchaseStatus,
    onBuyPremium: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.09f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                if (isPremium) "Premium actif" else "VibeCheck Premium",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                if (isPremium) "Merci. Aucune publicité." else "Supprime les pubs définitivement.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            if (!isPremium) {
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = onBuyPremium,
                    enabled = premiumReady,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    val label = when (purchaseStatus) {
                        PurchaseStatus.PENDING -> "Achat en attente"
                        PurchaseStatus.CANCELED -> "Réessayer"
                        PurchaseStatus.ERROR -> "Réessayer"
                        else -> if (premiumPrice != null) "Supprimer les pubs • $premiumPrice" else "Chargement…"
                    }
                    Text(label, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

private fun intensityLabel(value: GameIntensity): String = when (value) {
    GameIntensity.CHILL -> "Chill"
    GameIntensity.NORMAL -> "Normal"
    GameIntensity.SAVAGE -> "Savage"
}

private fun packLabel(value: GamePack): String = when (value) {
    GamePack.MIX -> "Mix"
    GamePack.FRIENDS -> "Friends"
    GamePack.DEEP -> "Deep"
    GamePack.CHAOS -> "Chaos"
}
