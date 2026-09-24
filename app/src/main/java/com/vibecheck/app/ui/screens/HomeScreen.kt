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
import androidx.compose.material.icons.rounded.Person
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
    onPlayTogether: () -> Unit = {},
    onPlaySolo: () -> Unit = {},
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(8.dp).background(MaterialTheme.colorScheme.tertiary, CircleShape))
                Text("VIBECHECK", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 2.2.sp)
            }
            Box(
                modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape).clickable(onClick = onOpenSettings),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = "Paramètres", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(21.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Avec qui veux-tu jouer ?", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(4.dp))
        Text("À plusieurs sur ce téléphone, ou en solo avec un casting simulé.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            EntryCard(
                title = "Jouer ensemble",
                subtitle = if (savedPlayerCount >= 2) "$savedPlayerCount joueurs prêts" else "2 à 8 joueurs",
                icon = Icons.Filled.Groups,
                onClick = onPlayTogether,
                modifier = Modifier.weight(1f)
            )
            EntryCard(
                title = "Jouer en solo",
                subtitle = "Casting simulé",
                icon = Icons.Rounded.Person,
                onClick = onPlaySolo,
                modifier = Modifier.weight(1f)
            )
        }

        if (savedPlayerCount >= 2) {
            Spacer(Modifier.height(10.dp))
            Text("Modifier le groupe", modifier = Modifier.clickable(onClick = onEditGroup), color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelLarge)
        }

        if (groupLeaderName != null && groupLeaderWins > 0) {
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenLeaderboard),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Leader du groupe", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                        Text("$groupLeaderName • $groupLeaderWins victoire${if (groupLeaderWins > 1) "s" else ""}", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelLarge)
                    }
                    Text("Classement", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Choisis un jeu", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            items(modeCards) { card -> ModeCard(card = card, onClick = { onMode(card.mode) }) }
            item {
                Spacer(Modifier.height(4.dp))
                Text("Ambiance", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GameIntensity.entries.forEach { intensity ->
                        CompactChoice(intensityLabel(intensity), intensity == selectedIntensity, { onIntensity(intensity) }, Modifier.weight(1f))
                    }
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
                Text("Pack", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GamePack.entries.forEach { pack -> CompactChoice(packLabel(pack), pack == selectedPack, { onPack(pack) }, Modifier.weight(1f)) }
                }
            }
            item {
                Spacer(Modifier.height(6.dp))
                PremiumCard(isPremium, premiumReady, premiumPrice, purchaseStatus, onBuyPremium)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EntryCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(128.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.20f))
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Column {
                Text(title, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private data class ModeCardModel(val mode: GameMode, val title: String, val subtitle: String, val icon: ImageVector, val accent: Color)

private val modeCards = listOf(
    ModeCardModel(GameMode.WHO_OF_US, "Who of Us", "Le groupe vote. Le verdict tombe.", Icons.Filled.Groups, Color(0xFFC9A7FF)),
    ModeCardModel(GameMode.MOST_LIKELY, "Most Likely", "Qui est le plus susceptible de… ?", Icons.Filled.Whatshot, Color(0xFFFFB88A)),
    ModeCardModel(GameMode.RED_GREEN, "Red / Green", "Red flag ou green flag ?", Icons.Filled.Bolt, Color(0xFF9BE6C1)),
    ModeCardModel(GameMode.KNOWS_ME, "Knows Me", "Qui te connaît vraiment ?", Icons.Filled.Psychology, Color(0xFF95C8FF))
)

@Composable
private fun ModeCard(card: ModeCardModel, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.985f else 1f, tween(120), label = "modeScale")
    Card(
        modifier = Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale }.clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, card.accent.copy(alpha = 0.20f))
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(Modifier.size(46.dp).background(card.accent.copy(alpha = 0.14f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Icon(card.icon, contentDescription = null, tint = card.accent, modifier = Modifier.size(25.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(card.title, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
                Text(card.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Text("→", color = card.accent, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun CompactChoice(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.18f)),
        shape = RoundedCornerShape(14.dp)
    ) { Text(label, Modifier.padding(vertical = 10.dp).fillMaxWidth(), color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge, textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
}

@Composable
private fun PremiumCard(isPremium: Boolean, premiumReady: Boolean, premiumPrice: String?, purchaseStatus: PurchaseStatus, onBuyPremium: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)), border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(if (isPremium) "Premium actif" else "VibeCheck Premium", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium)
            Text(if (isPremium) "Merci pour ton soutien." else "Supprime définitivement les pubs.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            if (!isPremium) {
                Spacer(Modifier.height(10.dp))
                Button(onClick = onBuyPremium, enabled = premiumReady, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)) {
                    Text(premiumPrice?.let { "Passer Premium • $it" } ?: "Passer Premium")
                }
                val status = when (purchaseStatus) {
                    PurchaseStatus.PENDING -> "Achat en attente"
                    PurchaseStatus.CANCELLED -> "Achat annulé"
                    PurchaseStatus.ERROR -> "Achat indisponible"
                    else -> null
                }
                status?.let { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}

private fun intensityLabel(value: GameIntensity) = when (value) {
    GameIntensity.CHILL -> "Chill"
    GameIntensity.NORMAL -> "Normal"
    GameIntensity.SAVAGE -> "Savage"
}

private fun packLabel(value: GamePack) = when (value) {
    GamePack.MIX -> "Mix"
    GamePack.FRIENDS -> "Friends"
    GamePack.DEEP -> "Deep"
    GamePack.CHAOS -> "Chaos"
}
