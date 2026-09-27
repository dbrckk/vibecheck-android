package com.vibecheck.app.ui.theme

import kotlin.math.roundToInt

object MotionPolicy {
    fun durationMillis(
        baseMillis: Int,
        animatorScale: Float,
    ): Int {
        if (baseMillis <= 0 || !animatorScale.isFinite() || animatorScale <= 0f) return 0
        return (baseMillis * animatorScale).roundToInt().coerceAtLeast(0)
    }
}
