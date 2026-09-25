package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.domain.solo.Persona
import com.vibecheck.app.domain.solo.PersonaKind
import com.vibecheck.app.ui.theme.VibeColors

@Composable
fun SoloPartyScreen(personas:List<Persona>,selectedIds:List<String>,selectedKind:PersonaKind?,query:String,playerName:String,validationMessage:String,canStart:Boolean,onBack:()->Unit,onQueryChange:(String)->Unit,onPlayerNameChange:(String)->Unit,onKindChange:(PersonaKind?)->Unit,onTogglePersona:(String)->Unit,onAutoCompose:()->Unit,onStart:()->Unit){
 Column(Modifier.fillMaxSize()){
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Button(onClick=onBack){Text("Retour")};Text("${selectedIds.size}/7 compagnons",color=VibeColors.TextSecondary,modifier=Modifier.padding(top=12.dp))}
  Spacer(Modifier.height(12.dp));Text("Ta soirée solo",style=MaterialTheme.typography.headlineMedium,color=VibeColors.TextPrimary);Text("Tu joues vraiment : tu réponds d’abord, puis les personnages simulés répondent.",color=VibeColors.TextSecondary)
  Spacer(Modifier.height(12.dp));Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Green.copy(alpha=.24f)),shape=RoundedCornerShape(26.dp)){Column(Modifier.padding(16.dp)){Text("Participant humain",fontWeight=FontWeight.Black,color=VibeColors.TextPrimary);OutlinedTextField(value=playerName,onValueChange=onPlayerNameChange,modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text("Moi / mon pseudo")});Text("Tes réponses sont les seules réponses réelles de cette partie.",color=VibeColors.TextSecondary,style=MaterialTheme.typography.bodyMedium)}}
  Spacer(Modifier.height(10.dp));Card(colors=CardDefaults.cardColors(containerColor=VibeColors.Rose.copy(alpha=.18f)),shape=RoundedCornerShape(22.dp)){Text("Simulation fictive : les réponses attribuées aux profils publics sont générées pour le divertissement. Elles ne viennent pas de ces personnes et ne représentent pas leurs opinions réelles.",modifier=Modifier.padding(14.dp),color=VibeColors.TextPrimary,fontWeight=FontWeight.Bold)}
  Spacer(Modifier.height(10.dp));OutlinedTextField(value=query,onValueChange=onQueryChange,modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text("Rechercher un compagnon")})
  Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){listOf<Pair<String,PersonaKind?>>("Tous" to null,"Public" to PersonaKind.PUBLIC_SIMULATION,"Originaux" to PersonaKind.ORIGINAL).forEach{(label,kind)->FilterChip(selected=selectedKind==kind,onClick={onKindChange(kind)},label={Text(label)})}}
  Button(onClick=onAutoCompose,modifier=Modifier.fillMaxWidth()){Text("Composer automatiquement")};if(validationMessage.isNotBlank())Text(validationMessage,color=VibeColors.Rose,fontWeight=FontWeight.Bold)
  LazyColumn(Modifier.weight(1f).fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(8.dp)){items(personas,key={it.id}){p->val selected=p.id in selectedIds;Card(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).semantics{contentDescription=p.displayName+if(selected)", sélectionné" else ""}.clickable{onTogglePersona(p.id)},colors=CardDefaults.cardColors(containerColor=if(selected)VibeColors.Purple.copy(alpha=.22f) else MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,if(selected)VibeColors.Purple else MaterialTheme.colorScheme.surfaceVariant)){Column(Modifier.padding(14.dp)){Text(p.displayName,fontWeight=FontWeight.ExtraBold,color=VibeColors.TextPrimary);Text(p.archetype,color=VibeColors.TextSecondary);if(p.isFictionalSimulation)Text("Simulation fictive",color=VibeColors.Rose,fontWeight=FontWeight.Bold)}}}}
  Button(onClick=onStart,enabled=canStart,modifier=Modifier.fillMaxWidth().height(58.dp),shape=RoundedCornerShape(22.dp)){Text("Je participe — démarrer",fontWeight=FontWeight.Black)}
 }
}
