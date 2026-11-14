package com.esportportal.controller

import com.esportportal.integration.pandascore.PandaScoreClient
import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.esportportal.integration.pandascore.PandaScoreSyncService
import org.springframework.web.bind.annotation.PostMapping
import com.esportportal.integration.pandascore.dto.PandaScoreIncidentDto
import java.time.Instant
import java.time.temporal.ChronoUnit
import com.esportportal.integration.pandascore.dto.PandaScoreDeletionDto

@RestController
class PandaScoreTestController(
    private val pandaScoreClient: PandaScoreClient,
    private val pandaScoreSyncService: PandaScoreSyncService

) {

    @GetMapping("/api/test/pandascore")
    fun testPandaScore(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzNadchodzaceMecze("lol")
    }

    @GetMapping("/api/test/pandascore/past")
    fun testPastMatches(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzZakonczoneMecze("lol")
    }

    @GetMapping("/api/test/pandascore/running")
    fun testRunningMatches(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzTrwajaceMecze("lol")
    }

    @PostMapping("/api/test/pandascore/sync")
    fun testSync(): String {
        pandaScoreSyncService.syncMatches()

        return "Synchronizacja zakończona"
    }
        @GetMapping("/api/test/pandascore/incidents")
        fun testIncidents(): List<PandaScoreIncidentDto> {
            val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

            return pandaScoreClient.pobierzZmianyMeczow(odKiedy, "lol")
        }
    @GetMapping("/api/test/pandascore/incidents/raw")
    fun testRawIncidents(): String {
        val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

        return pandaScoreClient.pobierzSuroweIncydenty(odKiedy, "cs2")
    }
    @GetMapping("/api/test/pandascore/additions")
    fun testAdditions(): List<PandaScoreIncidentDto> {
        val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

        return pandaScoreClient.pobierzNoweMecze(odKiedy, "lol")
    }
    @GetMapping("/api/test/pandascore/match")
    fun testMatch(): PandaScoreMatchDto? {
        return pandaScoreClient.pobierzMeczPoId(1795395)
    }
    @GetMapping("/api/test/pandascore/changes/check")
    fun testChangesCheck(): Boolean {
        val odKiedy = Instant.parse("2026-11-09T16:00:00Z")

        return pandaScoreClient.pobierzZmianyMeczow(odKiedy, "cs2")
            .any { it.id == 1795395L }
    }
    @PostMapping("/api/test/pandascore/match/sync")
    fun testSingleMatchSync(): Boolean {
        return pandaScoreSyncService.syncSingleMatch(1795395L, "cs2")
    }

    @GetMapping("/api/test/pandascore/deletions")
    fun testDeletions(): List<PandaScoreDeletionDto> {
        val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

        return pandaScoreClient.pobierzUsunieteMecze(
            odKiedy,
            "lol"
        )
    }
    }

