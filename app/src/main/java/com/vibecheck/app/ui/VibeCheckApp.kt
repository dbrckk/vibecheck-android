package com.vibecheck.app.ui

import android.app.Activity
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vibecheck.app.billing.PremiumBillingManager
import com.vibecheck.app.data.*
import com.vibecheck.app.domain.*
import com.vibecheck.app.domain.model.*
import com.vibecheck.app.domain.solo.PersonaAnswerEngine
import com.vibecheck.app.localization.AppLocale
import com.vibecheck.app.release.ReleaseConfig
import com.vibecheck.app.ui.screens.*
import com.vibecheck.app.ui.state.*
import com.vibecheck.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable fun VibeCheckApp(incomingChallenge:Challenge?=null,onChallengeConsumed:()->Unit={},gameViewModel:GameViewModel=viewModel(),soloPartyViewModel:SoloPartyViewModel=viewModel()){
 val context=LocalContext.current;val animatorScale=remember{runCatching{Settings.Global.getFloat(context.contentResolver,Settings.Global.ANIMATOR_DURATION_SCALE,1f)}.getOrDefault(1f).coerceAtLeast(0f)};val billing=remember{PremiumBillingManager(context.applicationContext)};val groups=remember{PlayerGroupStore(context.applicationContext)};val stats=remember{LocalStatsStore(context.applicationContext)};val onboarding=remember{OnboardingStore(context.applicationContext)};val appearance=remember{AppearanceStore(context.applicationContext)};val audio=remember{AudioPreferencesStore(context.applicationContext)};val theme by appearance.selectedStyle.collectAsState(initial=VibeThemeStyle.PASTEL_DRAWN);val musicEnabled by audio.musicEnabled.collectAsState();val musicVolume by audio.musicVolume.collectAsState();val scope=rememberCoroutineScope();var showOnboarding by remember{mutableStateOf(!onboarding.isCompleted())};var settingsOpen by rememberSaveable{mutableStateOf(false)};var soloOpen by rememberSaveable{mutableStateOf(false)}
 DisposableEffect(billing,ReleaseConfig.adRemovalPurchaseEnabled){if(ReleaseConfig.adRemovalPurchaseEnabled)billing.start();onDispose{if(ReleaseConfig.adRemovalPurchaseEnabled)billing.close()}}
 val premium by billing.isPremium.collectAsState();val ready by billing.isPurchaseReady.collectAsState();val price by billing.formattedPrice.collectAsState();val purchase by billing.purchaseStatus.collectAsState();val screenName by gameViewModel.screenName.collectAsState();val modeName by gameViewModel.modeName.collectAsState();val qi by gameViewModel.questionIndex.collectAsState();val rawVotes by gameViewModel.savedVotes.collectAsState();val players by gameViewModel.players.collectAsState();val targetRaw by gameViewModel.challengeTarget.collectAsState();val seed by gameViewModel.sessionSeed.collectAsState();val instance by gameViewModel.sessionInstanceId.collectAsState();val intensityName by gameViewModel.intensityName.collectAsState();val packName by gameViewModel.packName.collectAsState();val legacy by gameViewModel.legacyChallenge.collectAsState();val solo by gameViewModel.isSoloSession.collectAsState();val soloIds by gameViewModel.soloPersonaIds.collectAsState();val soloName by gameViewModel.soloPlayerName.collectAsState();val soloAnswer by gameViewModel.soloPlayerAnswer.collectAsState();val selectedIds by soloPartyViewModel.selectedIds.collectAsState();val visible by soloPartyViewModel.visiblePersonas.collectAsState();val query by soloPartyViewModel.query.collectAsState();val kind by soloPartyViewModel.kindFilter.collectAsState();val canStart by soloPartyViewModel.canStart.collectAsState();val validation by soloPartyViewModel.validationMessage.collectAsState();val setupName by soloPartyViewModel.playerName.collectAsState()
 val screen=runCatching{AppScreen.valueOf(screenName)}.getOrDefault(AppScreen.HOME);val mode=runCatching{GameMode.valueOf(modeName)}.getOrDefault(GameMode.WHO_OF_US);val intensity=runCatching{GameIntensity.valueOf(intensityName)}.getOrDefault(GameIntensity.NORMAL);val pack=runCatching{GamePack.valueOf(packName)}.getOrDefault(GamePack.MIX);val votes=SessionCodec.decodeVotes(rawVotes);val target=targetRaw.takeIf{it!=GameViewModel.NO_CHALLENGE};val sessionIntensity=if(legacy)null else intensity;val sessionPack=if(legacy)GamePack.MIX else pack;val personas=soloIds.mapNotNull{id->PersonaCatalog.all.firstOrNull{it.id==id}}
 LaunchedEffect(Unit){gameViewModel.restorePlayers(groups.load())};LaunchedEffect(players){groups.save(players)};LaunchedEffect(incomingChallenge){incomingChallenge?.let{gameViewModel.acceptChallenge(it);onChallengeConsumed()}}
 BackHandler(enabled=settingsOpen||soloOpen||screen!=AppScreen.HOME){when{settingsOpen->settingsOpen=false;soloOpen->soloOpen=false;else->gameViewModel.goHome()}}
 VibeCheckTheme(theme){Surface(Modifier.fillMaxSize(),color=Color.Transparent){VibeBackdrop(theme){Box(Modifier.fillMaxSize().safeDrawingPadding().padding(20.dp)){
  when{soloOpen&&screen==AppScreen.HOME->SoloPartyScreen(visible,selectedIds,kind,query,setupName,validation,canStart,{soloOpen=false},soloPartyViewModel::setQuery,soloPartyViewModel::setPlayerName,soloPartyViewModel::setKindFilter,soloPartyViewModel::toggle,{soloPartyViewModel.autoCompose(System.nanoTime(),5)},{if(canStart){gameViewModel.startSoloSession(selectedIds,GameMode.WHO_OF_US,System.currentTimeMillis(),setupName);soloOpen=false}})
   settingsOpen&&screen==AppScreen.HOME->SettingsScreen(theme,musicEnabled,musicVolume,{scope.launch{appearance.save(it)}},audio::setMusicEnabled,audio::setMusicVolume,{settingsOpen=false})
   showOnboarding&&incomingChallenge==null&&screen==AppScreen.HOME->OnboardingScreen{onboarding.markCompleted();showOnboarding=false}
   else->AnimatedContent(screen,transitionSpec={(fadeIn(tween(MotionPolicy.durationMillis(220,animatorScale)))+scaleIn(initialScale=.985f)).togetherWith(fadeOut())},label="screenTransition"){s->when(s){
    AppScreen.HOME->HomeScreen(premium,ready,price,purchase,intensity,pack,players.size,null,0,gameViewModel::openLeaderboard,gameViewModel::editPlayers,{settingsOpen=true},{soloOpen=true},{if(ReleaseConfig.adRemovalPurchaseEnabled)(context as? Activity)?.let{billing.launchPurchase(it)}},gameViewModel::selectIntensity,gameViewModel::selectPack,gameViewModel::quickStartMode)
    AppScreen.PLAYERS->PlayerSetupScreen(mode,players,target,gameViewModel::goBackHome,gameViewModel::addPlayer,gameViewModel::removePlayer,gameViewModel::startGame)
    AppScreen.GAME->{val questions=QuestionRepository.forMode(mode=mode,seed=seed,avoidIds=emptySet(),intensity=sessionIntensity,pack=sessionPack);if(questions.isEmpty())Text(AppLocale.pick("Questions indisponibles","Questions unavailable")) else{val i=qi.coerceIn(0,questions.lastIndex);val q=questions[i];if(solo){val options=if(mode==GameMode.RED_GREEN)listOf("Green Flag","Red Flag") else listOf(soloName)+personas.map{it.displayName};if(soloAnswer.isBlank())GameScreen(mode,AppLocale.pick("$soloName, à toi de répondre.\n\n${q.text}","$soloName, your turn to answer.\n\n${q.text}"),i+1,questions.size,options,gameViewModel::submitSoloPlayerAnswer,gameViewModel::abandonGame,animatorScale) else{val simulated=personas.map{PersonaAnswerEngine.answer(it,q.id,q.text,options,seed)};GameScreen(mode,AppLocale.pick("Les personnages répondent après toi.\n\n${q.text}","The characters answer after you.\n\n${q.text}"),i+1,questions.size,emptyList(),{},gameViewModel::abandonGame,animatorScale,simulated.mapIndexed{idx,a->SimulatedResponseUi(personas[idx].displayName,a.option,a.isFictionalSimulation)}){gameViewModel.submitSoloRound(q.id,simulated,i==questions.lastIndex)}}}else GameScreen(mode,q.text,i+1,questions.size,if(mode==GameMode.RED_GREEN)listOf("Green Flag","Red Flag") else players,{gameViewModel.answer(q.id,it,i==questions.lastIndex)},gameViewModel::abandonGame,animatorScale)}}
    AppScreen.RESULT->ResultScreen(mode,votes,null,target,seed,instance,intensity,pack,solo,animatorScale,gameViewModel::replay,gameViewModel::goHome)
    AppScreen.LEADERBOARD->LeaderboardScreen(players.map(stats::statFor),gameViewModel::goHome)
   }}}
  }
 }}}
}
