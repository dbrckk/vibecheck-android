package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.data.LocalStatsStore
import com.vibecheck.app.data.PlayerStat
import com.vibecheck.app.domain.*
import com.vibecheck.app.domain.model.*
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.sharing.ChallengeSharer
import com.vibecheck.app.sharing.ResultCardSharer
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.MotionPolicy
import com.vibecheck.app.ui.theme.VibeColors
import kotlinx.coroutines.launch

@Composable fun ResultScreen(mode:GameMode,votes:List<Vote>,resultOverride:GameResult?=null,challengeTarget:Int?,sessionSeed:Long,sessionInstanceId:Long,intensity:GameIntensity,pack:GamePack,isSoloSession:Boolean=false,animatorScale:Float=1f,onReplay:()->Unit,onHome:()->Unit){
 val result=resultOverride?:GameEngine.result(votes);val context=LocalContext.current;val stats=remember(context.applicationContext){LocalStatsStore(context.applicationContext)};val percent=if(result.total==0)0 else result.score*100/result.total;val won=challengeTarget!=null&&percent>=challengeTarget;val ranking=if(mode==GameMode.WHO_OF_US||mode==GameMode.MOST_LIKELY)GameEngine.ranking(votes).take(3) else emptyList();val accent=when(mode){GameMode.WHO_OF_US->VibeColors.Purple;GameMode.MOST_LIKELY->VibeColors.Orange;GameMode.RED_GREEN->VibeColors.Green;GameMode.KNOWS_ME->VibeColors.Blue};val reveal=remember{Animatable(0f)};val score=remember{Animatable(0f)};val scope=rememberCoroutineScope();var sharing by remember{mutableStateOf(false)};var shareError by remember{mutableStateOf(false)};var challengeError by remember{mutableStateOf(false)};var playerStat by remember(result.winner){mutableStateOf<PlayerStat?>(null)}
 LaunchedEffect(sessionInstanceId,result.winner,percent,mode){if(!isSoloSession&&mode!=GameMode.RED_GREEN&&result.winner.isNotBlank()&&result.winner!="Personne")playerStat=stats.recordResult(mode.name+"_"+sessionInstanceId,mode,result.winner,percent)}
 LaunchedEffect(percent){reveal.snapTo(0f);score.snapTo(0f);reveal.animateTo(1f,tween(MotionPolicy.durationMillis(280,animatorScale)));score.animateTo(percent.toFloat(),tween(MotionPolicy.durationMillis(620,animatorScale)))}
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(20.dp),horizontalAlignment=Alignment.CenterHorizontally){
  Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)){Box(Modifier.size(9.dp).background(accent,CircleShape));Text("VIBECHECK",color=accent,fontWeight=FontWeight.Black,letterSpacing=2.sp)}
  if(isSoloSession)Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Rose.copy(alpha=.16f)),border=BorderStroke(1.dp,VibeColors.Rose.copy(alpha=.32f)),shape=RoundedCornerShape(18.dp)){Column(Modifier.padding(14.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(AppLocale.pick("RÉSULTAT SOLO • SIMULATION FICTIVE","SOLO RESULT • FICTIONAL SIMULATION"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black,fontSize=11.sp);Text(AppLocale.pick("Ta réponse est réelle. Les réponses attribuées aux personnalités publiques sont simulées et ne représentent pas leurs opinions réelles.","Your answer is real. Answers attributed to public figures are simulated and do not represent their real opinions."),color=VibeColors.TextSecondary,textAlign=TextAlign.Center,fontSize=12.sp)}}
  Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,accent.copy(alpha=.24f)),elevation=CardDefaults.cardElevation(defaultElevation=4.dp),shape=RoundedCornerShape(34.dp),modifier=Modifier.fillMaxWidth().graphicsLayer{alpha=reveal.value}){Column(Modifier.fillMaxWidth().padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally){
   Card(colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.14f)),shape=RoundedCornerShape(16.dp)){Text(if(mode==GameMode.KNOWS_ME)AppLocale.pick("QUI CONNAÎT LE MIEUX ?","WHO KNOWS BEST?") else AppLocale.pick("LE GROUPE A PARLÉ","THE GROUP HAS SPOKEN"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black,fontSize=11.sp,modifier=Modifier.padding(horizontal=13.dp,vertical=7.dp))}
   Spacer(Modifier.height(18.dp));Text(result.winner,color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black,textAlign=TextAlign.Center);Text(score.value.toInt().toString()+"%",color=accent,fontSize=68.sp,fontWeight=FontWeight.Black);Text(if(mode==GameMode.KNOWS_ME)AppLocale.pick("de bonnes réponses • ${mode.title}","correct answers • ${mode.title}") else AppLocale.pick("des réponses • ${mode.title}","of answers • ${mode.title}"),color=VibeColors.TextSecondary,textAlign=TextAlign.Center)
   playerStat?.let{Spacer(Modifier.height(8.dp));Text(AppLocale.pick("${it.wins} victoire${if(it.wins==1)"" else "s"} locale${if(it.wins==1)"" else "s"}","${it.wins} local win${if(it.wins==1)"" else "s"}"),color=accent,fontWeight=FontWeight.Bold)}
   Spacer(Modifier.height(14.dp));Text("${pack.title} • ${intensity.title}",color=VibeColors.TextSecondary,fontWeight=FontWeight.Bold)
   if(ranking.isNotEmpty()&&votes.isNotEmpty()){Spacer(Modifier.height(16.dp));Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant.copy(alpha=.55f)),shape=RoundedCornerShape(22.dp),modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("TOP 3",color=VibeColors.TextSecondary,fontWeight=FontWeight.Black,fontSize=10.sp);ranking.forEachIndexed{i,e->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text("${i+1}. ${e.first}",color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold);Text("${e.second*100/votes.size}%",color=if(i==0)accent else VibeColors.TextSecondary,fontWeight=FontWeight.Black)}}}}}
   Spacer(Modifier.height(18.dp));LinearProgressIndicator(progress={(score.value/100f).coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth().height(9.dp),color=accent,trackColor=accent.copy(alpha=.13f))
   if(challengeTarget!=null){Spacer(Modifier.height(16.dp));Card(colors=CardDefaults.cardColors(containerColor=if(won)VibeColors.Green.copy(alpha=.2f) else VibeColors.Orange.copy(alpha=.18f)),shape=RoundedCornerShape(20.dp)){Text(if(won)AppLocale.pick("Défi réussi • objectif $challengeTarget% atteint","Challenge complete • $challengeTarget% target reached") else AppLocale.pick("Encore ${(challengeTarget-percent).coerceAtLeast(0)} points pour atteindre l’objectif","${(challengeTarget-percent).coerceAtLeast(0)} more points to reach the target"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center,modifier=Modifier.padding(14.dp))}}
  }}
  VibeActionSurface(onClick={if(!sharing){shareError=false;sharing=true;scope.launch{try{ResultCardSharer.share(context,mode,result.winner,percent,intensity,pack,playerStat?.wins?:0,playerStat?.bestScorePercent?:0,ranking,votes.size,isSoloSession)}catch(_:Exception){shareError=true}finally{sharing=false}}}},enabled=!sharing,modifier=Modifier.fillMaxWidth(),accent=accent,containerColor=accent,emphasized=true,minHeight=60.dp){Row(Modifier.fillMaxWidth().padding(16.dp),horizontalArrangement=Arrangement.Center,verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Share,null,tint=MaterialTheme.colorScheme.onPrimary);Spacer(Modifier.width(8.dp));Text(if(sharing)AppLocale.pick("Préparation...","Preparing...") else if(isSoloSession)AppLocale.pick("Partager la simulation fictive","Share fictional simulation") else AppLocale.pick("Partager le résultat","Share result"),color=MaterialTheme.colorScheme.onPrimary,fontWeight=FontWeight.Black)}}
  if(shareError)Text(AppLocale.pick("Le partage n'a pas pu être préparé. Réessaie.","The share could not be prepared. Try again."),color=VibeColors.Rose)
  if(!isSoloSession){VibeActionSurface(onClick={challengeError=false;try{ChallengeSharer.share(context,Challenge(mode,percent,sessionSeed,intensity,pack))}catch(_:Exception){challengeError=true}},modifier=Modifier.fillMaxWidth(),accent=accent,containerColor=MaterialTheme.colorScheme.surfaceVariant,minHeight=56.dp){Box(Modifier.fillMaxWidth().padding(15.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Défier un ami","Challenge a friend"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black)}};if(challengeError)Text(AppLocale.pick("Impossible d'ouvrir le partage du défi. Réessaie.","Could not open challenge sharing. Try again."),color=VibeColors.Rose)}
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){VibeActionSurface(onClick=onReplay,modifier=Modifier.weight(1f),accent=VibeColors.Purple,containerColor=VibeColors.PurpleBright,minHeight=52.dp){Box(Modifier.fillMaxWidth().padding(14.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Rejouer","Play again"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black)}};VibeActionSurface(onClick=onHome,modifier=Modifier.weight(1f),accent=accent,containerColor=MaterialTheme.colorScheme.surfaceVariant,minHeight=52.dp){Box(Modifier.fillMaxWidth().padding(14.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Modes","Modes"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black)}}}
 }
}
