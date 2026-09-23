package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameMode
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class Challenge(
    val mode: GameMode,
    val targetPercent: Int
)

object ChallengeLinkCodec {
    private const val SCHEME = "vibecheck"
    private const val HOST = "challenge"

    fun encode(challenge: Challenge): String {
        val mode = URLEncoder.encode(challenge.mode.name, StandardCharsets.UTF_8.name())
        val target = challenge.targetPercent.coerceIn(0, 100)
        return "$SCHEME://$HOST?mode=$mode&target=$target"
    }

    fun decode(raw: String?): Challenge? {
        if (raw.isNullOrBlank()) return null

        val uri = runCatching { URI(raw) }.getOrNull() ?: return null
        if (uri.scheme != SCHEME || uri.host != HOST) return null

        val params = uri.rawQuery
            ?.split("&")
            ?.mapNotNull { pair ->
                val index = pair.indexOf('=')
                if (index <= 0) return@mapNotNull null
                val key = URLDecoder.decode(pair.substring(0, index), StandardCharsets.UTF_8.name())
                val value = URLDecoder.decode(pair.substring(index + 1), StandardCharsets.UTF_8.name())
                key to value
            }
            ?.toMap()
            ?: return null

        val mode = runCatching { GameMode.valueOf(params["mode"].orEmpty()) }.getOrNull() ?: return null
        val target = params["target"]?.toIntOrNull()?.takeIf { it in 0..100 } ?: return null

        return Challenge(mode = mode, targetPercent = target)
    }
}
