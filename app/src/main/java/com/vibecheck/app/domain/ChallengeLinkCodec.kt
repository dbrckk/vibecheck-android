package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class Challenge(
    val mode: GameMode,
    val targetPercent: Int,
    val seed: Long = 0L,
    val intensity: GameIntensity? = null
)

object ChallengeLinkCodec {
    private const val SCHEME = "vibecheck"
    private const val HOST = "challenge"
    private const val VERSION = 2
    private const val MAX_LINK_LENGTH = 512
    private const val MAX_QUERY_PARTS = 8

    fun encode(challenge: Challenge): String {
        val mode = URLEncoder.encode(challenge.mode.name, StandardCharsets.UTF_8.name())
        val target = challenge.targetPercent.coerceIn(0, 100)
        val intensity = challenge.intensity
        return if (intensity == null) {
            "$SCHEME://$HOST?v=1&mode=$mode&target=$target&seed=${challenge.seed}"
        } else {
            "$SCHEME://$HOST?v=$VERSION&mode=$mode&target=$target&seed=${challenge.seed}&intensity=${intensity.name}"
        }
    }

    fun decode(raw: String?): Challenge? {
        if (raw.isNullOrBlank() || raw.length > MAX_LINK_LENGTH) return null

        val uri = runCatching { URI(raw) }.getOrNull() ?: return null
        if (!uri.scheme.equals(SCHEME, ignoreCase = true) ||
            !uri.host.equals(HOST, ignoreCase = true) ||
            uri.fragment != null ||
            !uri.path.isNullOrEmpty()
        ) return null

        val queryParts = uri.rawQuery?.split("&") ?: return null
        if (queryParts.isEmpty() || queryParts.size > MAX_QUERY_PARTS) return null

        val params = runCatching {
            val entries = queryParts.map { pair ->
                val index = pair.indexOf('=')
                require(index > 0)
                val key = URLDecoder.decode(
                    pair.substring(0, index),
                    StandardCharsets.UTF_8.name()
                )
                val value = URLDecoder.decode(
                    pair.substring(index + 1),
                    StandardCharsets.UTF_8.name()
                )
                key to value
            }
            require(entries.map { it.first }.distinct().size == entries.size)
            entries.toMap()
        }.getOrNull() ?: return null

        val version = params["v"]?.toIntOrNull() ?: 1
        if (version !in 1..VERSION) return null

        val mode = runCatching { GameMode.valueOf(params["mode"].orEmpty()) }.getOrNull() ?: return null
        val target = params["target"]?.toIntOrNull()?.takeIf { it in 0..100 } ?: return null
        val seed = params["seed"]?.toLongOrNull() ?: 0L
        val intensity = if (version >= 2) {
            runCatching {
                GameIntensity.valueOf(params["intensity"].orEmpty())
            }.getOrNull() ?: return null
        } else {
            null
        }

        return Challenge(
            mode = mode,
            targetPercent = target,
            seed = seed,
            intensity = intensity
        )
    }
}
