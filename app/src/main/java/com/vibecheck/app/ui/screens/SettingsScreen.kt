package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeColors
import com.vibecheck.app.ui.theme.VibeThemeStyle

@Composable
fun SettingsScreen(
    selectedStyle: VibeThemeStyle,
    onStyleSelected: (VibeThemeStyle) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Réglages",
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "Personnalise l'ambiance sans toucher à ta partie.",
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            TextButton(onClick = onBack) {
                Text("Retour")
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Apparence",
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Le thème s'applique immédiatement et reste sélectionné au prochain lancement.",
            color = VibeColors.TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = VibeThemeStyle.entries,
                key = { it.id },
            ) { style ->
                ThemePreviewCard(
                    style = style,
                    isSelected = style == selectedStyle,
                    onClick = { onStyleSelected(style) },
                )
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(
    style: VibeThemeStyle,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val preview = previewColors(style)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                selected = isSelected
                contentDescription = if (isSelected) {
                    "Thème ${style.label} sélectionné"
                } else {
                    "Choisir le thème ${style.label}"
                }
            },
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White.copy(alpha = 0.10f)
            },
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = if (isSelected) 0.09f else 0.045f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-6).dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                preview.forEachIndexed { index, color ->
                    Box(
                        modifier = Modifier
                            .size(if (index == 0) 34.dp else 30.dp)
                            .background(color, CircleShape),
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    style.label,
                    color = VibeColors.TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    styleDescription(style),
                    color = VibeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (isSelected) {
                Text(
                    "ACTIF",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private fun previewColors(style: VibeThemeStyle): List<Color> = when (style) {
    VibeThemeStyle.PREMIUM_DARK -> listOf(
        Color(0xFFC9A7FF),
        Color(0xFF95C8FF),
        Color(0xFF9BE6C1),
    )
    VibeThemeStyle.KAWAII -> listOf(
        Color(0xFFFFB8DF),
        Color(0xFFB9D9FF),
        Color(0xFFFFD39F),
    )
    VibeThemeStyle.POP -> listOf(
        Color(0xFFFFD44D),
        Color(0xFF70D7FF),
        Color(0xFFFF7A90),
    )
    VibeThemeStyle.ELEGANT -> listOf(
        Color(0xFFD8C29D),
        Color(0xFFB8B0A5),
        Color(0xFFE4D3B3),
    )
    VibeThemeStyle.STREET -> listOf(
        Color(0xFFB8FF3D),
        Color(0xFF7FE7D2),
        Color(0xFFFF9A3D),
    )
    VibeThemeStyle.MINIMAL -> listOf(
        Color(0xFFE7EAF0),
        Color(0xFFAEB7C5),
        Color(0xFFC8D0DC),
    )
}

private fun styleDescription(style: VibeThemeStyle): String = when (style) {
    VibeThemeStyle.PREMIUM_DARK -> "Profond, violet et cinématique"
    VibeThemeStyle.KAWAII -> "Rose pastel, doux et lumineux"
    VibeThemeStyle.POP -> "Énergique, contrasté et coloré"
    VibeThemeStyle.ELEGANT -> "Sobre, chaud et raffiné"
    VibeThemeStyle.STREET -> "Urbain, acide et percutant"
    VibeThemeStyle.MINIMAL -> "Neutre, net et concentré"
}
