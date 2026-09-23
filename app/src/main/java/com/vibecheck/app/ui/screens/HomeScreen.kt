package com.vibecheck.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Text("VibeCheck", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Black)
        Text("Qui connaît vraiment qui ?", color = Color(0xFFBEB7C9), fontSize = 18.sp)
        Spacer(Modifier.height(18.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isPremium) Color(0xFF2D2240) else Color(0xFF211E29)
            ),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    if (isPremium) "Premium actif" else "VibeCheck Premium",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (isPremium) {
                        "Aucune publicité sur ce compte Google Play."
                    } else {
                        "Supprime définitivement les publicités avec un achat unique."
                    },
                    color = Color(0xFFBEB7C9),
                    fontSize = 14.sp
                )

                if (!isPremium) {
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = onBuyPremium,
                        enabled = premiumReady,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (premiumReady) {
                                "Supprimer les pubs à vie" + (premiumPrice?.let { " • " + it } ?: "")
                            } else {
                                "Produit Play non disponible"
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(GameMode.entries) { mode ->
                Card(
                    onClick = { onMode(mode) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF211E29)),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(mode.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(mode.subtitle, color = Color(0xFFA9A3B3), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
