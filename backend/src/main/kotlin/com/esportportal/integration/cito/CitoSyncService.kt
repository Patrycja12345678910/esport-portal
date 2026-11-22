package com.esportportal.integration.cito

import com.esportportal.integration.TeamMatchingService
import com.esportportal.persistence.entity.SyncImportLogEntity
import com.esportportal.persistence.repository.SyncImportLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import java.time.Instant
import org.slf4j.LoggerFactory

@Service
class CitoSyncService (
    private val citoClient: CitoClient,
    private val teamMatchingService: TeamMatchingService,
    private val syncImportLogRepository: SyncImportLogRepository,
    @Value("\${cito.sync.page-size}") private val limit: Int,
    @Value("\${cito.sync.max-pages}") private val maxStron: Int

) {
    private val logger = LoggerFactory.getLogger(CitoSyncService::class.java)

    fun synchronizujDruzyny() {

        val log = SyncImportLogEntity()
        log.source = "cito"
        log.status = "RUNNING"
        log.startedAt = Instant.now()

        syncImportLogRepository.save(log)

        var strona = 1
        var liczbaDruzyn = 0
        var liczbaDopasowanychDruzyn = 0
        var liczbaOdrzuconychDruzyn = 0

        try {


//paginacja - kolejne strony z cito
            while (strona <= maxStron) {
                val response = citoClient.pobierzDruzyny(strona, limit)
                val druzyny = response.data

                if (druzyny.isEmpty()) {
                    break
                }

                liczbaDruzyn += druzyny.size

                druzyny.forEach { druzyna ->

                    if (druzyna.id.isBlank() || druzyna.name.isBlank()) {
                        liczbaOdrzuconychDruzyn++
                    } else {
                        val wynik = teamMatchingService.dopasujDruzyneCito(druzyna)



                        if (wynik != null) {
                            liczbaDopasowanychDruzyn++
                        }
                    }
                }

                if (strona >= response.meta.totalPages) {
                    break
                }
                strona++
            }

            log.status = "SUCCESS"
            log.finishedAt = Instant.now()
            log.fetchedCount = liczbaDruzyn
            log.matchedCount = liczbaDopasowanychDruzyn
            log.rejectedCount = liczbaOdrzuconychDruzyn

            syncImportLogRepository.save(log)

            val liczbaNiedopasowanychDruzyn = liczbaDruzyn - liczbaDopasowanychDruzyn - liczbaOdrzuconychDruzyn

            println("Pobrano drużyn: $liczbaDruzyn")
            println("Dopasowano drużyn: $liczbaDopasowanychDruzyn")
            println("Niedopasowano drużyn: $liczbaNiedopasowanychDruzyn")
            println("Odrzucono drużyn: $liczbaOdrzuconychDruzyn")

        } catch (e: Exception) {
            log.status = "FAILED"
            log.finishedAt = Instant.now()
            log.errorMessage = e.message
            log.fetchedCount = liczbaDruzyn
            log.matchedCount = liczbaDopasowanychDruzyn
            log.rejectedCount = liczbaOdrzuconychDruzyn

            syncImportLogRepository.save(log)

            throw e
        }
    }

    @Scheduled(cron = "\${cito.sync.cron}", zone = "Europe/Warsaw")
    fun automatycznaSynchronizacjaCito() {
        logger.info("Rozpoczynam automatyczną synchronizację Cito")

        try {
            synchronizujDruzyny()
            logger.info("Automatyczna synchronizacja Cito zakończona")
        } catch (e: Exception) {
           logger.error("Błąd automatycznej synchronizacji Cito", e)
        }

    }


    fun sprawdzDopasowania() {
        val druzyny = citoClient.pobierzDruzyny().data

        druzyny.forEach { druzyna ->
            val mapowanie = teamMatchingService.znajdzMapowanieCito(druzyna.id)

            val znaleziona = if (mapowanie != null) {
                mapowanie.team
            } else {
                teamMatchingService.znajdzDruzynePoNazwie(druzyna.name)
            }

            if(znaleziona != null) {
                println("Cito: ${druzyna.name} -> ${znaleziona.name}")
            } else {
                val kandydaci = teamMatchingService.znajdzKandydatowPoNazwie(druzyna.name)

                if(kandydaci.isEmpty()) {
                    println("Cito: ${druzyna.name} -> BRAK DOPASOWANIA")
                } else {
                    println("Cito: ${druzyna.name} -> KANDYDACI: ${kandydaci.map { it.name }}")
                }
            }

        }
    }

}
