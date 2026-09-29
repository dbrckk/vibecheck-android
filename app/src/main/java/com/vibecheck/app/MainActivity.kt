package com.vibecheck.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.ChallengeLinkCodec
import com.vibecheck.app.ui.VibeCheckApp

class MainActivity : ComponentActivity() {
    private var incomingChallenge by mutableStateOf<Challenge?>(null)
    private var appInForeground by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            incomingChallenge = ChallengeLinkCodec.decode(intent?.dataString)
        }

        setContent {
            VibeCheckApp(
                incomingChallenge = incomingChallenge,
                appInForeground = appInForeground,
                onChallengeConsumed = { incomingChallenge = null }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        appInForeground = true
    }

    override fun onStop() {
        appInForeground = false
        super.onStop()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        incomingChallenge = ChallengeLinkCodec.decode(intent.dataString)
    }
}
