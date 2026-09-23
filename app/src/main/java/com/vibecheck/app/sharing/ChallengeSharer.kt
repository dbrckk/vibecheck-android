package com.vibecheck.app.sharing

import android.content.Context
import android.content.Intent
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.ChallengeLinkCodec

object ChallengeSharer {
    fun share(context: Context, challenge: Challenge) {
        val link = ChallengeLinkCodec.encode(challenge)
        val text = "Je te défie sur VibeCheck 😈 Notre groupe a atteint ${challenge.targetPercent}% de consensus. À vous de faire mieux : $link"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(Intent.createChooser(intent, "Envoyer le challenge"))
    }
}
