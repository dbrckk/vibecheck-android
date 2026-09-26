package com.vibecheck.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Test

class MotionPolicyTest {
    @Test
    fun `zero animator scale disables motion durations`() {
        assertEquals(0, MotionPolicy.durationMillis(baseMillis = 280, animatorScale = 0f))
    }

    @Test
    fun `system animator scale proportionally adjusts duration`() {
        assertEquals(140, MotionPolicy.durationMillis(baseMillis = 280, animatorScale = 0.5f))
        assertEquals(560, MotionPolicy.durationMillis(baseMillis = 280, animatorScale = 2f))
    }

    @Test
    fun `invalid negative scale safely disables animation`() {
        assertEquals(0, MotionPolicy.durationMillis(baseMillis = 280, animatorScale = -1f))
    }
}
