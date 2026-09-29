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
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.PlayerStat
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.VibeColors
@Composable fun LeaderboardScreen(stats:List<PlayerStat>,onBack:()->Unit){Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(14.dp)){Text(AppLocale.pick("Classement du groupe","Group leaderboard"),color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black);Text(AppLocale.pick("Basé uniquement sur les parties jouées sur cet appareil.","Based only on games played on this device."),color=VibeColors.TextSecondary,fontSize=13.sp);if(stats.isEmpty()||stats.all{it.wins==0})Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=VibeColors.Orange.copy(alpha=.12f)),border=BorderStroke(1.dp,VibeColors.Orange.copy(alpha=.24f))){Text(AppLocale.pick("Joue quelques parties pour créer le classement.","Play a few games to build the leaderboard."),color=VibeColors.TextSecondary,modifier=Modifier.padding(20.dp))}else stats.forEachIndexed{i,s->val leader=i==0&&s.wins>0;Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=if(leader)VibeColors.Purple.copy(alpha=.16f) else MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,if(leader)VibeColors.Purple.copy(alpha=.34f) else MaterialTheme.colorScheme.surfaceVariant)){Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){Text("#${i+1}",color=if(leader)VibeColors.Purple else VibeColors.TextSecondary,fontWeight=FontWeight.Black,fontSize=18.sp);Column(Modifier.weight(1f)){Text(s.name,color=VibeColors.TextPrimary,fontWeight=FontWeight.Black);Text(AppLocale.pick("${s.wins} victoire${if(s.wins==1)"" else "s"}","${s.wins} win${if(s.wins==1)"" else "s"}"),color=VibeColors.TextSecondary,fontSize=12.sp)};Text("${s.bestScorePercent}%",color=VibeColors.Green,fontWeight=FontWeight.Black)}}};Spacer(Modifier.weight(1f));VibeActionSurface(onClick=onBack,modifier=Modifier.fillMaxWidth(),accent=VibeColors.Purple,containerColor=MaterialTheme.colorScheme.surface,minHeight=56.dp){Box(Modifier.fillMaxWidth().padding(15.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Retour","Back"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black)}}}}
