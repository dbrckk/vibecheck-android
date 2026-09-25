package com.vibecheck.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeColors
import com.vibecheck.app.ui.theme.VibeThemeStyle

@Composable fun SettingsScreen(selectedStyle:VibeThemeStyle,onStyleSelected:(VibeThemeStyle)->Unit,onBack:()->Unit){Column(Modifier.fillMaxSize()){Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){Column(Modifier.weight(1f)){Text("Réglages",color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineMedium);Text("Choisis l'ambiance de VibeCheck.",color=VibeColors.TextSecondary)};TextButton(onClick=onBack){Text("Retour")}};Spacer(Modifier.height(22.dp));Text("Apparence",color=VibeColors.TextPrimary,style=MaterialTheme.typography.titleLarge);Text("Le thème s'applique immédiatement et reste sélectionné.",color=VibeColors.TextSecondary);Spacer(Modifier.height(14.dp));LazyColumn(Modifier.fillMaxWidth(),verticalArrangement=Arrangement.spacedBy(10.dp)){items(VibeThemeStyle.entries,key={it.id}){style->ThemePreviewCard(style,style==selectedStyle){onStyleSelected(style)}}}}}
@Composable private fun ThemePreviewCard(style:VibeThemeStyle,isSelected:Boolean,onClick:()->Unit){val preview=previewColors(style);Card(onClick=onClick,modifier=Modifier.fillMaxWidth().semantics{selected=isSelected;contentDescription=if(isSelected)"Thème ${style.label} sélectionné" else "Choisir le thème ${style.label}"},shape=RoundedCornerShape(26.dp),border=BorderStroke(if(isSelected)2.dp else 1.dp,if(isSelected)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)){Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){Row(horizontalArrangement=Arrangement.spacedBy((-6).dp)){preview.forEachIndexed{i,c->Box(Modifier.size(if(i==0)34.dp else 30.dp).background(c,CircleShape))}};Column(Modifier.weight(1f)){Text(style.label,color=VibeColors.TextPrimary,style=MaterialTheme.typography.titleMedium);Text(styleDescription(style),color=VibeColors.TextSecondary)};if(isSelected)Text("ACTIF",color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Black)}}}
private fun previewColors(style:VibeThemeStyle)=when(style){VibeThemeStyle.PASTEL_DRAWN->listOf(Color(0xFFE8D7FF),Color(0xFFF7C9D9),Color(0xFFFFC8AD),Color(0xFFBFE5D0));VibeThemeStyle.PREMIUM_DARK->listOf(Color(0xFFC9A7FF),Color(0xFF95C8FF),Color(0xFF9BE6C1));VibeThemeStyle.KAWAII->listOf(Color(0xFFFFB8DF),Color(0xFFB9D9FF),Color(0xFFFFD39F));VibeThemeStyle.POP->listOf(Color(0xFFFFD44D),Color(0xFF70D7FF),Color(0xFFFF7A90));VibeThemeStyle.ELEGANT->listOf(Color(0xFFD8C29D),Color(0xFFB8B0A5),Color(0xFFE4D3B3));VibeThemeStyle.STREET->listOf(Color(0xFFB8FF3D),Color(0xFF7FE7D2),Color(0xFFFF9A3D));VibeThemeStyle.MINIMAL->listOf(Color(0xFFE7EAF0),Color(0xFFAEB7C5),Color(0xFFC8D0DC))}
private fun styleDescription(style:VibeThemeStyle)=when(style){VibeThemeStyle.PASTEL_DRAWN->"Lavande, rose poudré, pêche, menthe et crème";VibeThemeStyle.PREMIUM_DARK->"Profond, violet et cinématique";VibeThemeStyle.KAWAII->"Rose pastel, doux et lumineux";VibeThemeStyle.POP->"Énergique, contrasté et coloré";VibeThemeStyle.ELEGANT->"Sobre, chaud et raffiné";VibeThemeStyle.STREET->"Urbain, acide et percutant";VibeThemeStyle.MINIMAL->"Neutre, net et concentré"}
