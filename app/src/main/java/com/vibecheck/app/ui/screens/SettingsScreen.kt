package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeBackdrop
import com.vibecheck.app.ui.theme.VibeThemeStyle
import com.vibecheck.app.ui.theme.themeTokens

@Composable
fun SettingsScreen(
    selectedTheme: VibeThemeStyle,
    onThemeSelected: (VibeThemeStyle) -> Unit,
    onBack: () -> Unit
) {
    VibeBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Retour",
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = onBack)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(18.dp))
            Text(
                text = "Apparence",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Choisis l'ambiance qui te ressemble. Le changement est instantané.",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(22.dp))

            VibeThemeStyle.entries.forEach { style ->
                ThemePreviewCard(
                    style = style,
                    selected = style == selectedTheme,
                    onClick = { onThemeSelected(style) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(
    style: VibeThemeStyle,
    selected: Boolean,
    onClick: () -> Unit
) {
    val token = themeTokens(style)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = Color(token.surface),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) Color(token.primary) else Color(token.textSecondary).copy(alpha = 0.22f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(token.background)),
                contentAlignment = Alignment.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(token.primary, token.secondary, token.tertiary).forEach { color ->
                        Box(
                            Modifier
                                .size(11.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = style.label,
                    color = Color(token.textPrimary),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = themeDescription(style),
                    color = Color(token.textSecondary),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (selected) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = Color(token.primary)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Thème sélectionné",
                            tint = Color(token.onPrimary),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun themeDescription(style: VibeThemeStyle): String = when (style) {
    VibeThemeStyle.PREMIUM_DARK -> "Sombre, profond et cinématique"
    VibeThemeStyle.KAWAII -> "Pastel, doux et lumineux"
    VibeThemeStyle.POP -> "Énergique, coloré et contrasté"
    VibeThemeStyle.ELEGANT -> "Chaleureux, sobre et raffiné"
    VibeThemeStyle.STREET -> "Urbain, vif et audacieux"
    VibeThemeStyle.MINIMAL -> "Clair, calme et essentiel"
}
