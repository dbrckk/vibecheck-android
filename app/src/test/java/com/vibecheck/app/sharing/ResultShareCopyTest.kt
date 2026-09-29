package com.vibecheck.app.sharing

import com.vibecheck.app.domain.model.GameMode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultShareCopyTest {
    @Test fun `solo copy clearly says simulation and avoids real opinion framing`() {
        val text = ResultShareCopy.english(
            mode = GameMode.WHO_OF_US,
            winner = "Zendaya",
            percent = 62,
            podium = "1. Zendaya 62% • 2. Me 38%",
            fictionalSimulation = true,
        )
        assertTrue(text.contains("fictional", ignoreCase = true))
        assertTrue(text.contains("simulat", ignoreCase = true))
        assertTrue(text.contains("not their real opinions", ignoreCase = true))
        assertFalse(text.contains("really thinks", ignoreCase = true))
        assertFalse(text.contains("genuine opinion", ignoreCase = true))
    }

    @Test fun `group copy includes winner score and podium when available`() {
        val text = ResultShareCopy.english(
            mode = GameMode.MOST_LIKELY,
            winner = "Alex",
            percent = 70,
            podium = "1. Alex 70% • 2. Sam 30%",
            fictionalSimulation = false,
        )
        assertTrue(text.contains("Alex"))
        assertTrue(text.contains("70%"))
        assertTrue(text.contains("1. Alex 70%"))
    }
}
