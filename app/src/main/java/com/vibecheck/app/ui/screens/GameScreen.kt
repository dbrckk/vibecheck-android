package com.vibecheck.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.ui.theme.MotionPolicy
import com.vibecheck.app.ui.theme.VibeColors

data class SimulatedResponseUi(val personaName:String,val option:String,val isFictionalSimulation:Boolean)

@Composable fun GameScreen(mode:GameMode,questionText:String,progress:Int,total:Int,answers:List<String>,onAnswer:(String)->Unit,onExit:()->Unit,animatorScale:Float=1f,simulatedResponses:List<SimulatedResponseUi> = emptyList(),onContinueSimulation:(()->Unit)?=null){
 val fraction=if(total<=0)0f else progress.toFloat()/total;val animated by animateFloatAsState(fraction.coerceIn(0f,1f),tween(MotionPolicy.durationMillis(280,animatorScale)),label="questionProgress");val reveal=remember{Animatable(1f)};val accent=when(mode){GameMode.WHO_OF_US->VibeColors.Purple;GameMode.MOST_LIKELY->VibeColors.Orange;GameMode.RED_GREEN->VibeColors.Green;GameMode.KNOWS_ME->VibeColors.Blue};val size=when{questionText.length>110->23.sp;questionText.length>80->25.sp;questionText.length>55->27.sp;else->30.sp};val line=when{questionText.length>110->29.sp;questionText.length>80->31.sp;questionText.length>55->33.sp;else->36.sp};val haptic=LocalHapticFeedback.current;var answering by remember(progress,questionText){mutableStateOf(false)}
 LaunchedEffect(progress){reveal.snapTo(0f);reveal.animateTo(1f,tween(MotionPolicy.durationMillis(220,animatorScale)))}
 fun submit(answer:String){if(answering)return;answering=true;haptic.performHapticFeedback(HapticFeedbackType.LongPress);onAnswer(answer)}
 Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.SpaceBetween){
  Column{Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Column{Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)){Box(Modifier.size(9.dp).background(accent,CircleShape));Text(mode.title,color=accent,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Black)};Text("Question $progress sur $total",color=VibeColors.TextSecondary,fontSize=13.sp)};TextButton(onClick=onExit,shape=RoundedCornerShape(16.dp),colors=ButtonDefaults.textButtonColors(contentColor=VibeColors.TextSecondary)){Text("Quitter",fontWeight=FontWeight.Bold)}};Spacer(Modifier.height(10.dp));LinearProgressIndicator(progress={animated},modifier=Modifier.fillMaxWidth().height(8.dp),color=accent,trackColor=accent.copy(alpha=.14f))}
  Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,accent.copy(alpha=.24f)),elevation=CardDefaults.cardElevation(defaultElevation=4.dp),shape=RoundedCornerShape(34.dp),modifier=Modifier.fillMaxWidth().graphicsLayer{alpha=reveal.value;translationY=(1f-reveal.value)*24f}){Column(Modifier.padding(horizontal=22.dp,vertical=30.dp),horizontalAlignment=Alignment.CenterHorizontally){Card(colors=CardDefaults.cardColors(containerColor=accent.copy(alpha=.14f)),shape=RoundedCornerShape(999.dp)){Text("CHOISIS SANS TROP RÉFLÉCHIR",color=VibeColors.TextPrimary,fontSize=11.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp,modifier=Modifier.padding(horizontal=13.dp,vertical=7.dp))};Spacer(Modifier.height(18.dp));Text(questionText,color=VibeColors.TextPrimary,fontSize=size,lineHeight=line,fontWeight=FontWeight.Black,textAlign=TextAlign.Center)}}
  Spacer(Modifier.height(20.dp))
  if(simulatedResponses.isNotEmpty())LazyColumn(Modifier.fillMaxWidth().weight(1f).graphicsLayer{alpha=reveal.value},verticalArrangement=Arrangement.spacedBy(10.dp)){
   item{Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Rose.copy(alpha=.14f)),border=BorderStroke(1.dp,VibeColors.Rose.copy(alpha=.28f)),shape=RoundedCornerShape(20.dp),modifier=Modifier.fillMaxWidth()){Text("Simulation fictive hors ligne : les réponses des profils publics sont générées pour le jeu et ne représentent pas leurs opinions réelles.",color=VibeColors.TextSecondary,fontSize=12.sp,lineHeight=17.sp,modifier=Modifier.padding(14.dp))}}
   itemsIndexed(simulatedResponses,key={i,r->"$i:${r.personaName}"}){_,r->Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,if(r.isFictionalSimulation)VibeColors.Rose.copy(alpha=.25f) else VibeColors.Green.copy(alpha=.25f)),shape=RoundedCornerShape(22.dp),modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(15.dp)){Text("${r.personaName} → ${r.option}",color=VibeColors.TextPrimary,fontSize=16.sp,fontWeight=FontWeight.Bold);Text(if(r.isFictionalSimulation)"Simulation fictive" else "Personnage original",color=if(r.isFictionalSimulation)VibeColors.Rose else VibeColors.Green,fontSize=11.sp,fontWeight=FontWeight.Bold)}}}
   item{Button(onClick={onContinueSimulation?.invoke()},enabled=onContinueSimulation!=null,modifier=Modifier.fillMaxWidth().height(58.dp),shape=RoundedCornerShape(22.dp),colors=ButtonDefaults.buttonColors(containerColor=accent,contentColor=MaterialTheme.colorScheme.onPrimary)){Text("Voir le résultat global",fontWeight=FontWeight.Black)}}
  } else LazyColumn(Modifier.fillMaxWidth().weight(1f).graphicsLayer{alpha=reveal.value},verticalArrangement=Arrangement.spacedBy(10.dp)){itemsIndexed(answers,key={i,a->"$i:$a"}){index,answer->val source=remember(answer,progress){MutableInteractionSource()};val pressed by source.collectIsPressedAsState();val scale by animateFloatAsState(if(pressed).985f else 1f,tween(MotionPolicy.durationMillis(if(pressed)80 else 130,animatorScale)),label="answerScale");if(mode==GameMode.RED_GREEN)OutlinedButton(onClick={submit(answer)},enabled=!answering,interactionSource=source,modifier=Modifier.fillMaxWidth().height(62.dp).graphicsLayer{scaleX=scale;scaleY=scale},shape=RoundedCornerShape(22.dp),border=BorderStroke(1.5.dp,if(index==0)VibeColors.Green else VibeColors.Rose),colors=ButtonDefaults.outlinedButtonColors(contentColor=VibeColors.TextPrimary,containerColor=if(index==0)VibeColors.Green.copy(alpha=.12f) else VibeColors.Rose.copy(alpha=.12f))){Text(answer,fontSize=17.sp,fontWeight=FontWeight.Bold)} else Button(onClick={submit(answer)},enabled=!answering,interactionSource=source,modifier=Modifier.fillMaxWidth().height(62.dp).graphicsLayer{scaleX=scale;scaleY=scale},shape=RoundedCornerShape(22.dp),colors=ButtonDefaults.buttonColors(containerColor=accent.copy(alpha=.18f),contentColor=VibeColors.TextPrimary,disabledContainerColor=accent.copy(alpha=.08f),disabledContentColor=VibeColors.TextSecondary)){Text(answer,fontSize=17.sp,fontWeight=FontWeight.Bold)}}}
 }
}
