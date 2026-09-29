package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeColors
import com.vibecheck.app.localization.AppLocale

@Composable
fun OnboardingScreen(onStart: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement=Arrangement.Center, horizontalAlignment=Alignment.CenterHorizontally) {
        Text("VIBECHECK",color=VibeColors.Purple,style=MaterialTheme.typography.labelLarge,fontWeight=FontWeight.Black)
        Spacer(Modifier.height(12.dp))
        Text(AppLocale.pick("Ton groupe. Vos réponses. Le verdict.","Your group. Your answers. The verdict."),color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black)
        Spacer(Modifier.height(8.dp))
        Text(AppLocale.pick("Une soirée légère, colorée et sans compte à créer.","A light, colorful party game with no account required."),color=VibeColors.TextSecondary,style=MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(22.dp))
        Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(32.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,VibeColors.Purple.copy(alpha=.20f)),elevation=CardDefaults.cardElevation(defaultElevation=3.dp)) {
            Column(Modifier.fillMaxWidth().padding(22.dp),verticalArrangement=Arrangement.spacedBy(17.dp)) {
                OnboardingLine("1",AppLocale.pick("Choisis comment tu veux jouer.","Choose how you want to play."),VibeColors.Purple)
                OnboardingLine("2",AppLocale.pick("Réponds avec tes amis ou participe en solo.","Answer with friends or take part in solo mode."),VibeColors.Rose)
                OnboardingLine("3",AppLocale.pick("Découvre le verdict et partage-le si tu veux.","See the verdict and share it if you want."),VibeColors.Green)
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(onClick=onStart,modifier=Modifier.fillMaxWidth().height(60.dp),shape=RoundedCornerShape(24.dp),colors=ButtonDefaults.buttonColors(containerColor=VibeColors.Purple,contentColor=MaterialTheme.colorScheme.onPrimary)){Text(AppLocale.pick("Commencer","Start"),fontWeight=FontWeight.Black)}
        Spacer(Modifier.height(10.dp));Text(AppLocale.pick("Pas de compte requis • fonctionne hors ligne","No account required • works offline"),color=VibeColors.TextSecondary,style=MaterialTheme.typography.bodySmall)
    }
}

@Composable private fun OnboardingLine(number:String,text:String,accent:androidx.compose.ui.graphics.Color){
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically){
        Card(shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.18f))){Text(number,color=VibeColors.TextPrimary,fontWeight=FontWeight.Black,modifier=Modifier.padding(horizontal=13.dp,vertical=9.dp))}
        Text(text,color=VibeColors.TextPrimary,style=MaterialTheme.typography.bodyLarge,modifier=Modifier.weight(1f))
    }
}
