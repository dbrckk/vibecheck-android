package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChallengeLinkCodecTest {
    @Test
    fun challenge_round_trip_preserves_values() {
        val challenge = Challenge(GameMode.WHO_OF_US, 63, seed = 987654321L)
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
    fun preserves_seed_for_fair_question_order() {
        val decoded = ChallengeLinkCodec.decode(
            ChallengeLinkCodec.encode(
                Challenge(GameMode.MOST_LIKELY, 55, seed = -42L)
            )
        )

        assertEquals(-42L, decoded?.seed)
    }

    @Test
    fun rejects_unknown_version() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?v=99&mode=WHO_OF_US&target=63&seed=12"
            )
        )
    }

    @Test
    fun old_link_without_seed_remains_readable() {
        val decoded = ChallengeLinkCodec.decode(
            "vibecheck://challenge?mode=WHO_OF_US&target=63"
        )

        assertEquals(0L, decoded?.seed)
    }

    @Test
    fun clamps_target_when_encoding() {
        val decoded = ChallengeLinkCodec.decode(
            ChallengeLinkCodec.encode(Challenge(GameMode.RED_GREEN, 150))
        )
        assertEquals(100, decoded?.targetPercent)
    }
}
