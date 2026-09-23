package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun OnboardingScreen(
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "VIBECHECK",
            color = VibeColors.Purple,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Ton groupe. Vos réponses. Le verdict.",
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )

        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF21B1721)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OnboardingLine("1", "Choisis un mode, un pack et une intensité.")
                OnboardingLine("2", "Répondez à 8 questions ensemble.")
                OnboardingLine("3", "Compare le résultat et partage la Story.")
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VibeColors.PurpleBright,
                contentColor = Color(0xFF18121F)
            )
        ) {
            Text("Commencer", fontWeight = FontWeight.Black)
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Pas de compte requis • fonctionne hors ligne",
            color = VibeColors.TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun OnboardingLine(number: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(999.dp),
            colors = CardDefaults.cardColors(
                containerColor = VibeColors.Purple.copy(alpha = 0.16f)
            )
        ) {
            Text(
                number,
                color = VibeColors.PurpleBright,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        Text(
            text,
            color = VibeColors.TextPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}
