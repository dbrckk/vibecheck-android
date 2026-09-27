package com.vibecheck.app.release

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReleaseConfigTest {
    @Test fun `v1 uses unique Play application id`() { assertEquals("com.dbrckk.vibecheck", ReleaseConfig.applicationId) }
    @Test fun `v1 1 uses production version metadata`() { assertEquals(2, ReleaseConfig.versionCode); assertEquals("1.1.0", ReleaseConfig.versionName) }
    @Test fun `v1 targets current Android API and keeps broad device support`() { assertEquals(36, ReleaseConfig.targetSdk); assertTrue(ReleaseConfig.minSdk <= 24) }
    @Test fun `backup is disabled for privacy safe offline v1`() { assertTrue(!ReleaseConfig.allowBackup) }
    @Test fun `cleartext network traffic is disabled`() { assertTrue(!ReleaseConfig.usesCleartextTraffic) }
    @Test fun `ad removal purchase is disabled while v1 ships without ads`() { assertTrue(!ReleaseConfig.adRemovalPurchaseEnabled) }
}
