package com.esportportal.integration.pandascore

import org.springframework.stereotype.Component

@Component
class PandaScoreRateLimiter {

    private var ostatnieZapytanie = 0L


    @Synchronized
    fun czekajNaMozliwoscZapytania() {
        val teraz = System.currentTimeMillis()

        val minimalnyOdstep = 1000L
        val czasOdOstatniegoZapytania = teraz - ostatnieZapytanie

        if( czasOdOstatniegoZapytania < minimalnyOdstep) {
             val czasOczekiwania = minimalnyOdstep - czasOdOstatniegoZapytania
            Thread.sleep(czasOczekiwania)
        }
        ostatnieZapytanie = System.currentTimeMillis()
    }

}