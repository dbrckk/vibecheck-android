package com.vibecheck.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioPreferencesStoreContractTest {
    @Test fun `music defaults are enabled and quiet`() {
        assertTrue(AudioPreferencesDefaults.ENABLED)
        assertEquals(0.18f, AudioPreferencesDefaults.VOLUME, 0.0001f)
    }

    @Test fun `volume sanitizer clamps persisted and ui values`() {
        assertEquals(0f, AudioPreferencesDefaults.sanitizeVolume(-0.5f), 0f)
        assertEquals(0.42f, AudioPreferencesDefaults.sanitizeVolume(0.42f), 0f)
        assertEquals(1f, AudioPreferencesDefaults.sanitizeVolume(2f), 0f)
    }

    @Test fun `preference contract uses stable dedicated keys`() {
        assertEquals("vibecheck_audio", AudioPreferencesDefaults.PREFS_NAME)
        assertEquals("music_enabled", AudioPreferencesDefaults.KEY_ENABLED)
        assertEquals("music_volume", AudioPreferencesDefaults.KEY_VOLUME)
        assertFalse(AudioPreferencesDefaults.KEY_ENABLED == AudioPreferencesDefaults.KEY_VOLUME)
    }
}
