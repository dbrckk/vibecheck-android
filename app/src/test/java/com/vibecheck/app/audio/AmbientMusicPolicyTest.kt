package com.vibecheck.app.audio

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AmbientMusicPolicyTest {
    @Test fun `plays only when enabled foreground and focused`() {
        assertTrue(AmbientMusicPolicy(true, true, true).shouldPlay)
        assertFalse(AmbientMusicPolicy(false, true, true).shouldPlay)
        assertFalse(AmbientMusicPolicy(true, false, true).shouldPlay)
        assertFalse(AmbientMusicPolicy(true, true, false).shouldPlay)
    }

    @Test fun `disabling prevents resume after focus returns`() {
        val disabled = AmbientMusicPolicy(enabled = false, foreground = true, hasAudioFocus = false)
        assertFalse(disabled.copy(hasAudioFocus = true).shouldPlay)
    }

    @Test fun `background always pauses even with focus`() {
        assertFalse(AmbientMusicPolicy(enabled = true, foreground = false, hasAudioFocus = true).shouldPlay)
    }
}
