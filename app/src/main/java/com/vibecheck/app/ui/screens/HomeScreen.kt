package com.vibecheck.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.GameMode

@Composable
fun HomeScreen(
    isPremium: Boolean,
    premiumReady: Boolean,
    premiumPrice: String?,
    onBuyPremium: () -> Unit,
    onMode: (GameMode) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "VIBECHECK",
            color = Color(0xFFC9A7FF),
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.2.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Qui connaît vraiment qui ?",
            color = Color.White,
            fontSize = 31.sp,
            lineHeight = 35.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            "8 questions. Un groupe. Zéro filtre.",
            color = Color(0xFFAAA2B5),
            fontSize = 15.sp
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
            containerColor = if (isPremium) Color(0xFF2C2436) else Color(0xFF1E1A25)
        ),
        shape = RoundedCornerShape(22.dp),
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
                    .background(Color(0xFF4B2E6B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = Color(0xFFE8D8FF)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isPremium) "Premium actif" else "VibeCheck Premium",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    if (isPremium) {
                        "Expérience sans publicité."
                    } else {
                        "Supprime les pubs définitivement."
                    },
                    color = Color(0xFFAAA2B5),
                    fontSize = 13.sp
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

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = visual.background),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(visual.accent.copy(alpha = 0.16f), CircleShape),
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    mode.subtitle,
                    color = Color(0xFFB1A9BB),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Text(
                "›",
                color = visual.accent,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light
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
            accent = Color(0xFFC9A7FF),
            background = Color(0xFF211B2B)
        )
        GameMode.MOST_LIKELY -> ModeVisual(
            icon = Icons.Default.Whatshot,
            accent = Color(0xFFFFB88A),
            background = Color(0xFF281D1A)
        )
        GameMode.RED_GREEN -> ModeVisual(
            icon = Icons.Default.Bolt,
            accent = Color(0xFF9BE6C1),
            background = Color(0xFF19251F)
        )
        GameMode.KNOWS_ME -> ModeVisual(
            icon = Icons.Default.Psychology,
            accent = Color(0xFF95C8FF),
            background = Color(0xFF192231)
        )
    }
