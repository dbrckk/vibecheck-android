package com.vibecheck.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.vibecheck.app.domain.PlayerRules
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.ui.components.VibeActionSurface
import com.vibecheck.app.ui.theme.VibeColors

@Composable fun PlayerSetupScreen(mode:GameMode,players:List<String>,challengeTarget:Int?,onBack:()->Unit,onAddPlayer:(String)->Unit,onRemovePlayer:(String)->Unit,onStart:()->Unit){
 var input by rememberSaveable{mutableStateOf("")};val normalized=PlayerRules.normalize(input);val canAdd=PlayerRules.canAdd(players,input);val canStart=PlayerRules.canStart(players);val progress=players.size.toFloat()/PlayerRules.MAX_PLAYERS
 Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.SpaceBetween){Column(Modifier.weight(1f)){Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(9.dp)){Box(Modifier.size(9.dp).background(VibeColors.Purple,CircleShape));Text(AppLocale.pick("Crée ton groupe","Create your group"),color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge)};Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(AppLocale.pick("${PlayerRules.MIN_PLAYERS} à ${PlayerRules.MAX_PLAYERS} joueurs","${PlayerRules.MIN_PLAYERS} to ${PlayerRules.MAX_PLAYERS} players"),color=VibeColors.TextSecondary);Text("${players.size}/${PlayerRules.MAX_PLAYERS}",color=VibeColors.Purple,fontWeight=FontWeight.Black)};Spacer(Modifier.height(8.dp));LinearProgressIndicator(progress={progress.coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth().height(8.dp),color=VibeColors.Purple,trackColor=VibeColors.Purple.copy(alpha=.13f));challengeTarget?.let{Spacer(Modifier.height(10.dp));Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Purple.copy(alpha=.14f)),border=BorderStroke(1.dp,VibeColors.Purple.copy(alpha=.3f)),shape=RoundedCornerShape(20.dp)){Text(AppLocale.pick("Défi reçu • ${if(mode==GameMode.KNOWS_ME)"score" else "consensus"} cible : $it%","Challenge received • target ${if(mode==GameMode.KNOWS_ME)"score" else "consensus"}: $it%"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold,modifier=Modifier.padding(14.dp))}};Spacer(Modifier.height(16.dp));OutlinedTextField(value=input,onValueChange={input=it.take(PlayerRules.MAX_NAME_LENGTH+4)},label={Text(AppLocale.pick("Prénom ou pseudo","Name or nickname"))},supportingText={Text("${input.length}/${PlayerRules.MAX_NAME_LENGTH}")},singleLine=true,keyboardOptions=KeyboardOptions(imeAction=ImeAction.Done),keyboardActions=KeyboardActions(onDone={if(canAdd){onAddPlayer(normalized);input=""}}),shape=RoundedCornerShape(20.dp),modifier=Modifier.fillMaxWidth());Spacer(Modifier.height(8.dp));VibeActionSurface(onClick={onAddPlayer(normalized);input=""},enabled=canAdd&&players.size<PlayerRules.MAX_PLAYERS,modifier=Modifier.fillMaxWidth(),accent=VibeColors.Purple,containerColor=VibeColors.Purple.copy(alpha=.18f),minHeight=54.dp){Box(Modifier.fillMaxWidth().padding(14.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Ajouter le joueur","Add player"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Black)}};Spacer(Modifier.height(14.dp));if(players.isEmpty())Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Orange.copy(alpha=.13f)),border=BorderStroke(1.dp,VibeColors.Orange.copy(alpha=.25f)),shape=RoundedCornerShape(22.dp),modifier=Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text(AppLocale.pick("Ton groupe est vide","Your group is empty"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold);Text(AppLocale.pick("Ajoute ${PlayerRules.MIN_PLAYERS} joueurs minimum pour commencer.","Add at least ${PlayerRules.MIN_PLAYERS} players to start."),color=VibeColors.TextSecondary)}} else LazyColumn(Modifier.fillMaxWidth().weight(1f).animateContentSize(),verticalArrangement=Arrangement.spacedBy(8.dp)){items(players,key={it}){p->Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,VibeColors.Purple.copy(alpha=.16f)),shape=RoundedCornerShape(20.dp),modifier=Modifier.fillMaxWidth().animateItem()){Row(Modifier.fillMaxWidth().padding(start=15.dp,end=5.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)){Box(Modifier.size(32.dp).background(VibeColors.Purple.copy(alpha=.16f),CircleShape),contentAlignment=Alignment.Center){Text(p.take(1).uppercase(),color=VibeColors.Purple,fontWeight=FontWeight.Black)};Text(p,color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold)};IconButton(onClick={onRemovePlayer(p)}){Icon(Icons.Default.Close,AppLocale.pick("Supprimer $p","Remove $p"),tint=VibeColors.TextSecondary)}}}}}}
  Column(verticalArrangement=Arrangement.spacedBy(8.dp)){VibeActionSurface(onClick=onStart,enabled=canStart,modifier=Modifier.fillMaxWidth(),accent=VibeColors.Purple,containerColor=VibeColors.Purple,emphasized=true,minHeight=60.dp){Box(Modifier.fillMaxWidth().padding(16.dp),contentAlignment=Alignment.Center){Text(if(canStart)AppLocale.pick("Lancer la partie","Start game") else AppLocale.pick("Ajoute au moins ${PlayerRules.MIN_PLAYERS} joueurs","Add at least ${PlayerRules.MIN_PLAYERS} players"),color=MaterialTheme.colorScheme.onPrimary,fontWeight=FontWeight.Black)}};VibeActionSurface(onClick=onBack,modifier=Modifier.fillMaxWidth(),accent=VibeColors.Purple,containerColor=MaterialTheme.colorScheme.surface,minHeight=52.dp){Box(Modifier.fillMaxWidth().padding(13.dp),contentAlignment=Alignment.Center){Text(AppLocale.pick("Retour","Back"),color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold)}}}
 }
}
