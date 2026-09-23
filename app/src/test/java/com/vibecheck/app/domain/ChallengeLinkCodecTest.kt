package com.vibecheck.app.domain

import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.model.GamePack
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
    fun rejects_malformed_percent_encoding() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?mode=WHO_OF_US&target=%ZZ"
            )
        )
    }

    @Test
    fun rejects_duplicate_parameters() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?mode=WHO_OF_US&mode=RED_GREEN&target=63"
            )
        )
    }

    @Test
    fun rejects_unexpected_path_or_fragment() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge/extra?mode=WHO_OF_US&target=63"
            )
        )
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?mode=WHO_OF_US&target=63#extra"
            )
        )
    }

    @Test
    fun rejects_oversized_link() {
        val padding = "x".repeat(600)
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?mode=WHO_OF_US&target=63&padding=$padding"
            )
        )
    }

    @Test
    fun accepts_scheme_and_host_case_insensitively() {
        val decoded = ChallengeLinkCodec.decode(
            "VIBECHECK://CHALLENGE?mode=WHO_OF_US&target=63"
        )
        assertEquals(GameMode.WHO_OF_US, decoded?.mode)
        assertEquals(63, decoded?.targetPercent)
    }

    @Test
    fun clamps_target_when_encoding() {
        val decoded = ChallengeLinkCodec.decode(
            ChallengeLinkCodec.encode(Challenge(GameMode.RED_GREEN, 150))
        )
        assertEquals(100, decoded?.targetPercent)
    }
    @Test
    fun v2_round_trip_preserves_intensity() {
        val challenge = Challenge(
            mode = GameMode.MOST_LIKELY,
            targetPercent = 75,
            seed = 4242L,
            intensity = GameIntensity.SAVAGE
        )

        val decoded = ChallengeLinkCodec.decode(ChallengeLinkCodec.encode(challenge))

        assertEquals(challenge, decoded)
    }

    @Test
    fun legacy_v1_link_keeps_null_intensity() {
        val decoded = ChallengeLinkCodec.decode(
            "vibecheck://challenge?v=1&mode=WHO_OF_US&target=63&seed=12"
        )

        assertNull(decoded?.intensity)
    }

    @Test
    fun v2_requires_valid_intensity() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?v=2&mode=WHO_OF_US&target=63&seed=12&intensity=EXTREME"
            )
        )
    }
    @Test
    fun v3_round_trip_preserves_pack() {
        val challenge = Challenge(
            mode = GameMode.WHO_OF_US,
            targetPercent = 80,
            seed = 123L,
            intensity = GameIntensity.NORMAL,
            pack = GamePack.CHAOS
        )

        assertEquals(challenge, ChallengeLinkCodec.decode(ChallengeLinkCodec.encode(challenge)))
    }

    @Test
    fun v2_link_keeps_null_pack() {
        val decoded = ChallengeLinkCodec.decode(
            "vibecheck://challenge?v=2&mode=WHO_OF_US&target=63&seed=12&intensity=NORMAL"
        )

        assertNull(decoded?.pack)
    }

    @Test
    fun v3_requires_valid_pack() {
        assertNull(
            ChallengeLinkCodec.decode(
                "vibecheck://challenge?v=3&mode=WHO_OF_US&target=63&seed=12&intensity=NORMAL&pack=UNKNOWN"
            )
        )
    }
}
