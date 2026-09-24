package com.vibecheck.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.vibecheck.app.data.AppearanceStore
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.ChallengeLinkCodec
import com.vibecheck.app.ui.VibeCheckApp
import com.vibecheck.app.ui.theme.VibeCheckTheme
import com.vibecheck.app.ui.theme.VibeThemeStyle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var incomingChallenge by mutableStateOf<Challenge?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            incomingChallenge = ChallengeLinkCodec.decode(intent?.dataString)
        }

        setContent {
            val appearanceStore = remember(applicationContext) {
                AppearanceStore(applicationContext)
            }
            val selectedTheme by appearanceStore.theme.collectAsState(
                initial = VibeThemeStyle.PREMIUM_DARK
            )
            val scope = rememberCoroutineScope()

            VibeCheckTheme(style = selectedTheme) {
                VibeCheckApp(
                    incomingChallenge = incomingChallenge,
                    onChallengeConsumed = { incomingChallenge = null },
                    selectedTheme = selectedTheme,
                    onThemeSelected = { style ->
                        scope.launch { appearanceStore.setTheme(style) }
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        incomingChallenge = ChallengeLinkCodec.decode(intent.dataString)
    }
}
