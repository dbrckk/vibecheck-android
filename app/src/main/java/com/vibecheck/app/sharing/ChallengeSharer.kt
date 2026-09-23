package com.vibecheck.app.sharing

import android.content.Context
import android.content.Intent
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.ChallengeLinkCodec

object ChallengeSharer {
    fun share(context: Context, challenge: Challenge) {
        val link = ChallengeLinkCodec.encode(challenge)
        val metric = if (challenge.mode == com.vibecheck.app.domain.model.GameMode.KNOWS_ME) {
            "un score de ${challenge.targetPercent}%"
        } else {
            "${challenge.targetPercent}% de consensus"
        }
        val text = "Je te défie sur VibeCheck 😈 Notre groupe a atteint $metric. À vous de faire mieux : $link"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(Intent.createChooser(intent, "Envoyer le challenge"))
    }
}
