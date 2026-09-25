package com.vibecheck.app.release

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReleaseConfigTest {
    @Test
    fun `v1 uses production version metadata`() {
        assertEquals(1, ReleaseConfig.versionCode)
        assertEquals("1.0.0", ReleaseConfig.versionName)
    }

    @Test
    fun `v1 targets current Android API and keeps broad device support`() {
        assertEquals(36, ReleaseConfig.targetSdk)
        assertTrue(ReleaseConfig.minSdk <= 24)
    }

    @Test
    fun `backup is disabled for privacy safe offline v1`() {
        assertTrue(!ReleaseConfig.allowBackup)
    }

    @Test
    fun `cleartext network traffic is disabled`() {
        assertTrue(!ReleaseConfig.usesCleartextTraffic)
    }
}
