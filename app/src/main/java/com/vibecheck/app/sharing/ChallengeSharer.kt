package com.vibecheck.app.sharing

import android.content.Context
import android.content.Intent
import com.vibecheck.app.domain.Challenge
import com.vibecheck.app.domain.ChallengeLinkCodec
import com.vibecheck.app.localization.AppLocale

object ChallengeSharer {
    fun share(context: Context, challenge: Challenge) {
        val link = ChallengeLinkCodec.encode(challenge)
        val metric = if (challenge.mode == com.vibecheck.app.domain.model.GameMode.KNOWS_ME) {
            AppLocale.pick("un score de ${challenge.targetPercent}%","a score of ${challenge.targetPercent}%")
        } else {
            AppLocale.pick("${challenge.targetPercent}% de consensus","${challenge.targetPercent}% consensus")
        }
        val text = AppLocale.pick("Je te défie sur VibeCheck 😈 Notre groupe a atteint $metric. À vous de faire mieux : $link","I challenge you on VibeCheck 😈 Our group reached $metric. Beat us: $link")

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(Intent.createChooser(intent, AppLocale.pick("Envoyer le challenge","Send challenge")))
    }
}
