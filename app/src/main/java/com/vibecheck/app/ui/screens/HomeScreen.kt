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
import androidx.compose.material.icons.filled.Whatshot
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
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun HomeScreen(
    isPremium: Boolean,
    premiumReady: Boolean,
    premiumPrice: String?,
    onBuyPremium: () -> Unit,
    onMode: (GameMode) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
        Spacer(Modifier.height(8.dp))
        Text(
            "Qui connaît vraiment qui ?",
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "8 questions. Un groupe. Zéro filtre.",
            color = Color(0xFFAAA2B5),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(18.dp))

        PremiumCard(
            isPremium = isPremium,
            premiumReady = premiumReady,
            premiumPrice = premiumPrice,
            onBuyPremium = onBuyPremium
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
                ModeCard(mode = mode, onClick = { onMode(mode) })
            }
        }
    }
}

@Composable
private fun PremiumCard(
    isPremium: Boolean,
    premiumReady: Boolean,
    premiumPrice: String?,
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
                    if (isPremium) {
                        "Expérience sans publicité."
                    } else {
                        "Supprime les pubs définitivement."
                    },
                    color = Color(0xFFAAA2B5),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!isPremium) {
                Button(
                    onClick = onBuyPremium,
                    enabled = premiumReady,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEEE6FF),
                        contentColor = Color(0xFF18121F)
                    )
                ) {
                    Text(
                        premiumPrice ?: if (premiumReady) "Premium" else "—",
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        mode.title,
                        color = VibeColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "8 QUESTIONS",
                        color = visual.accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                }
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
