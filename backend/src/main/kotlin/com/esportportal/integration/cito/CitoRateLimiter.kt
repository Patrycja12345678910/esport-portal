package com.esportportal.integration.cito

import org.springframework.stereotype.Component
import com.esportportal.persistence.repository.ApiRequestLogRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import com.esportportal.persistence.entity.ApiRequestLogEntity

@Component
class CitoRateLimiter (
    private val apiRequestLogRepository: ApiRequestLogRepository,
) {

    private var ostatnieZapytanie = 0L

    @Synchronized
    fun czekajNaMozliwoscZapytania() {
        val teraz = System.currentTimeMillis()

        val minimalnyOdstep = 6100L
        val czasOdOstatniegoZapytania = teraz - ostatnieZapytanie

        if( czasOdOstatniegoZapytania < minimalnyOdstep) {
            val czasOczekiwania = minimalnyOdstep - czasOdOstatniegoZapytania
            Thread.sleep(czasOczekiwania)
        }
        sprawdzLimitMiesieczny()
        zapiszZapytanie()

        ostatnieZapytanie = System.currentTimeMillis()
    }

    private fun sprawdzLimitMiesieczny() {
        val odKiedy = Instant.now().minus(30, ChronoUnit.DAYS)

        val liczbaZapytan = apiRequestLogRepository.countBySourceAndRequestedAtAfter("cito", odKiedy)

        val limitMiesieczny = 500L

        if(liczbaZapytan >= limitMiesieczny) {
            throw IllegalStateException("Osiągnięto limit zapytań")
        }


    }

    private fun zapiszZapytanie() {
        val log = ApiRequestLogEntity()

        log.source = "cito"
        log.requestedAt = Instant.now()

        apiRequestLogRepository.save(log)
    }

}
