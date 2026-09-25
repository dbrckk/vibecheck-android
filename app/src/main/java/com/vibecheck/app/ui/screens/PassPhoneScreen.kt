package com.vibecheck.app.ui.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vibecheck.app.ui.theme.VibeColors
import com.vibecheck.app.localization.AppLocale
@Composable fun PassPhoneScreen(nextPlayer:String,promptLabel:String,onReady:()->Unit){Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.Center,horizontalAlignment=Alignment.CenterHorizontally){Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(34.dp),border=BorderStroke(1.dp,VibeColors.Blue.copy(alpha=.3f)),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),elevation=CardDefaults.cardElevation(defaultElevation=4.dp)){Column(Modifier.fillMaxWidth().padding(horizontal=24.dp,vertical=30.dp),horizontalAlignment=Alignment.CenterHorizontally){Box(Modifier.size(60.dp).background(VibeColors.Blue.copy(alpha=.16f),CircleShape),contentAlignment=Alignment.Center){Icon(Icons.Default.VisibilityOff,null,tint=VibeColors.Blue)};Spacer(Modifier.height(18.dp));Text(AppLocale.pick("Passe le téléphone à","Pass the phone to"),color=VibeColors.TextSecondary,style=MaterialTheme.typography.bodyLarge);Text(nextPlayer,color=VibeColors.TextPrimary,style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black,textAlign=TextAlign.Center);Spacer(Modifier.height(12.dp));Text(promptLabel,color=VibeColors.TextSecondary,textAlign=TextAlign.Center);Spacer(Modifier.height(22.dp));Button(onClick=onReady,modifier=Modifier.fillMaxWidth().height(58.dp),shape=RoundedCornerShape(22.dp),colors=ButtonDefaults.buttonColors(containerColor=VibeColors.Blue,contentColor=MaterialTheme.colorScheme.onPrimary)){Text(AppLocale.pick("$nextPlayer est prêt","$nextPlayer is ready"),fontWeight=FontWeight.Black)}}}}}
