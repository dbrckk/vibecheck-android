package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChallengeLinkCodecTest {
    @Test
    fun challenge_round_trip_preserves_values() {
        val challenge = Challenge(GameMode.WHO_OF_US, 63)
        assertEquals(challenge, ChallengeLinkCodec.decode(ChallengeLinkCodec.encode(challenge)))
    }

    @Test
    fun rejects_unknown_scheme() {
        assertNull(ChallengeLinkCodec.decode("https://example.com/challenge?mode=WHO_OF_US&target=63"))
    }

    @Test
    fun rejects_invalid_target() {
        assertNull(ChallengeLinkCodec.decode("vibecheck://challenge?mode=WHO_OF_US&target=120"))
    }

    @Test
    fun clamps_target_when_encoding() {
        val decoded = ChallengeLinkCodec.decode(
            ChallengeLinkCodec.encode(Challenge(GameMode.RED_GREEN, 150))
        )
        assertEquals(100, decoded?.targetPercent)
    }
}
