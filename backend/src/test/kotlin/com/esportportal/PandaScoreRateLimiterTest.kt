package com.esportportal

import com.esportportal.integration.pandascore.PandaScoreRateLimiter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

class PandaScoreRateLimiterTest {

    @Test
    fun testOdstepuMiedzyZapytaniami() {
        val limiter = PandaScoreRateLimiter()

        limiter.czekajNaMozliwoscZapytania()

        val start = System.currentTimeMillis()

        limiter.czekajNaMozliwoscZapytania()

        val koniec = System.currentTimeMillis()

        val czasOczekiwania = koniec - start

        assertTrue(czasOczekiwania >= 950)
    }

}