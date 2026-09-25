package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
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
    onOpenSettings: () -> Unit,
    onPlaySolo: () -> Unit,
    onBuyPremium: () -> Unit,
    onIntensity: (GameIntensity) -> Unit,
    onPack: (GamePack) -> Unit,
    onMode: (GameMode) -> Unit
) {
    var showTogetherOptions by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(VibeColors.Green, CircleShape)
                )
                Text(
                    "VIBECHECK",
                    color = VibeColors.Purple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.2.sp
                )
            }
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ouvrir les réglages",
                    tint = VibeColors.TextSecondary,
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "Qui connaît vraiment qui ?",
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Choisis comment tu veux jouer, puis configure seulement ce dont tu as besoin.",
            color = Color(0xFFAAA2B5),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(18.dp))
        PlayEntryCard(
            title = "Jouer ensemble",
            subtitle = if (savedPlayerCount >= 2) {
                "$savedPlayerCount joueurs mémorisés • configuration rapide"
            } else {
                "Passe le téléphone et compare les réponses du groupe"
            },
            icon = Icons.Default.Groups,
            accent = VibeColors.Purple,
            emphasized = true,
            onClick = { showTogetherOptions = true },
        )
        Spacer(Modifier.height(10.dp))
        PlayEntryCard(
            title = "Jouer en solo",
            subtitle = "Compose ton groupe virtuel et joue entièrement hors ligne",
            icon = Icons.Default.Psychology,
            accent = VibeColors.Blue,
            emphasized = false,
            onClick = onPlaySolo,
        )

        if (savedPlayerCount >= 2) {
            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = VibeColors.Green.copy(alpha = 0.10f)
                ),
                border = BorderStroke(1.dp, VibeColors.Green.copy(alpha = 0.28f)),
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
                        color = VibeColors.TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = onEditGroup,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VibeColors.Green.copy(alpha = 0.18f),
                            contentColor = VibeColors.TextPrimary
                        )
                    ) {
                        Text("Modifier", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (groupLeaderName != null && groupLeaderWins > 0) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Leader local : " + groupLeaderName + " • " + groupLeaderWins +
                            if (groupLeaderWins == 1) " victoire" else " victoires",
                        color = VibeColors.TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = onOpenLeaderboard,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VibeColors.Purple.copy(alpha = 0.16f),
                            contentColor = VibeColors.TextPrimary
                        )
                    ) {
                        Text("Classement", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        PremiumCard(
            isPremium = isPremium,
            premiumReady = premiumReady,
            premiumPrice = premiumPrice,
            purchaseStatus = purchaseStatus,
            onBuyPremium = onBuyPremium
        )

        if (showTogetherOptions) {
            Spacer(Modifier.height(18.dp))

            Text(
                "PACK",
                color = Color(0xFF8F8799),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(Modifier.height(9.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GamePack.entries.chunked(2).forEach { rowPacks ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowPacks.forEach { pack ->
                            val selected = pack == selectedPack
                            Button(
                                onClick = { onPack(pack) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selected) {
                                        VibeColors.Blue
                                    } else {
                                        Color.White.copy(alpha = 0.055f)
                                    },
                                    contentColor = if (selected) {
                                        Color(0xFF11151C)
                                    } else {
                                        VibeColors.TextPrimary
                                    }
                                )
                            ) {
                                Text(pack.title, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        if (rowPacks.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                selectedPack.subtitle,
                color = VibeColors.TextSecondary,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "INTENSITÉ",
                color = Color(0xFF8F8799),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(Modifier.height(9.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GameIntensity.entries.forEach { intensity ->
                    val selected = intensity == selectedIntensity
                    Button(
                        onClick = { onIntensity(intensity) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) {
                                VibeColors.Purple
                            } else {
                                Color.White.copy(alpha = 0.055f)
                            },
                            contentColor = if (selected) {
                                Color(0xFF18121F)
                            } else {
                                VibeColors.TextPrimary
                            }
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 6.dp,
                            vertical = 10.dp
                        )
                    ) {
                        Text(
                            intensity.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                selectedIntensity.subtitle,
                color = VibeColors.TextSecondary,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(18.dp))
            Text(
                "CHOISIS TON VIBE",
                color = Color(0xFF8F8799),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(GameMode.entries) { mode ->
                    ModeCard(
                        mode = mode,
                        expressStart = savedPlayerCount >= 2 || mode == GameMode.RED_GREEN,
                        onClick = { onMode(mode) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayEntryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    emphasized: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (emphasized) {
                accent.copy(alpha = 0.16f)
            } else {
                Color.White.copy(alpha = 0.055f)
            }
        ),
        border = BorderStroke(
            width = if (emphasized) 2.dp else 1.dp,
            color = accent.copy(alpha = if (emphasized) 0.48f else 0.22f),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (emphasized) 9.dp else 4.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(accent.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accent,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    subtitle,
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                "→",
                color = accent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }
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
        colors = CardDefaults.cardColors(
            containerColor = if (isPremium) Color(0xFF2C2436) else Color(0xE61B1721)
        ),
        border = BorderStroke(1.dp, if (isPremium) VibeColors.Purple.copy(alpha = 0.42f) else Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (isPremium) VibeColors.Purple.copy(alpha = 0.22f) else Color(0xFF4B2E6B),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = VibeColors.PurpleBright
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isPremium) "Premium actif" else "VibeCheck Premium",
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    when {
                        isPremium -> "Expérience sans publicité."
                        purchaseStatus == PurchaseStatus.PENDING -> "Achat en attente de validation Google Play."
                        purchaseStatus == PurchaseStatus.CANCELLED -> "Achat annulé. Tu peux réessayer quand tu veux."
                        purchaseStatus == PurchaseStatus.ERROR -> "Google Play est temporairement indisponible."
                        purchaseStatus == PurchaseStatus.LOADING -> "Connexion à Google Play…"
                        else -> "Supprime les pubs définitivement."
                    },
                    color = Color(0xFFAAA2B5),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!isPremium) {
                Button(
                    onClick = onBuyPremium,
                    enabled = premiumReady &&
                        purchaseStatus != PurchaseStatus.PENDING &&
                        purchaseStatus != PurchaseStatus.LOADING,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEE6FF),
                        contentColor = Color(0xFF18121F)
                    )
                ) {
                    Text(
                        when (purchaseStatus) {
                            PurchaseStatus.PENDING -> "En attente"
                            PurchaseStatus.LOADING -> "…"
                            else -> premiumPrice ?: if (premiumReady) "Premium" else "—"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeCard(
    mode: GameMode,
    expressStart: Boolean,
    onClick: () -> Unit
) {
    val visual = modeVisual(mode)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = if (isPressed) 90 else 150),
        label = "modeCardScale"
    )

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = visual.background),
        border = BorderStroke(1.dp, visual.accent.copy(alpha = 0.20f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp, pressedElevation = 2.dp),
        shape = RoundedCornerShape(26.dp),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(visual.accent.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = visual.icon,
                    contentDescription = null,
                    tint = visual.accent
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    mode.title,
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    if (expressStart) "8 QUESTIONS • DÉMARRAGE DIRECT" else "8 QUESTIONS",
                    color = visual.accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.8.sp
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    mode.subtitle,
                    color = Color(0xFFB1A9BB),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Text(
                "→",
                color = visual.accent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class ModeVisual(
    val icon: ImageVector,
    val accent: Color,
    val background: Color
)

private fun modeVisual(mode: GameMode): ModeVisual =
    when (mode) {
        GameMode.WHO_OF_US -> ModeVisual(
            icon = Icons.Default.Groups,
            accent = VibeColors.Purple,
            background = Color(0xFF211B2B)
        )
        GameMode.MOST_LIKELY -> ModeVisual(
            icon = Icons.Default.Whatshot,
            accent = VibeColors.Orange,
            background = Color(0xFF281D1A)
        )
        GameMode.RED_GREEN -> ModeVisual(
            icon = Icons.Default.Bolt,
            accent = VibeColors.Green,
            background = Color(0xFF19251F)
        )
        GameMode.KNOWS_ME -> ModeVisual(
            icon = Icons.Default.Psychology,
            accent = VibeColors.Blue,
            background = Color(0xFF192231)
        )
    }
