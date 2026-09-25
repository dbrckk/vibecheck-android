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
import com.vibecheck.app.ui.theme.VibeColors
@Composable fun LeaderboardScreen(stats:List<PlayerStat>,onBack:()->Unit){Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(14.dp)){Text("Classement du groupe",color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black);Text("Basé uniquement sur les parties jouées sur cet appareil.",color=VibeColors.TextSecondary,fontSize=13.sp);if(stats.isEmpty()||stats.all{it.wins==0})Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=VibeColors.Orange.copy(alpha=.12f)),border=BorderStroke(1.dp,VibeColors.Orange.copy(alpha=.24f))){Text("Joue quelques parties pour créer le classement.",color=VibeColors.TextSecondary,modifier=Modifier.padding(20.dp))}else stats.forEachIndexed{i,s->val leader=i==0&&s.wins>0;Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=if(leader)VibeColors.Purple.copy(alpha=.16f) else MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,if(leader)VibeColors.Purple.copy(alpha=.34f) else MaterialTheme.colorScheme.surfaceVariant)){Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){Text("#${i+1}",color=if(leader)VibeColors.Purple else VibeColors.TextSecondary,fontWeight=FontWeight.Black,fontSize=18.sp);Column(Modifier.weight(1f)){Text(s.name,color=VibeColors.TextPrimary,fontWeight=FontWeight.Black);Text("${s.wins} victoire${if(s.wins==1)"" else "s"}",color=VibeColors.TextSecondary,fontSize=12.sp)};Text("${s.bestScorePercent}%",color=VibeColors.Green,fontWeight=FontWeight.Black)}}};Spacer(Modifier.weight(1f));OutlinedButton(onClick=onBack,modifier=Modifier.fillMaxWidth().height(56.dp),shape=RoundedCornerShape(20.dp),border=BorderStroke(1.dp,VibeColors.Purple.copy(alpha=.2f))){Text("Retour",color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold)}}}
