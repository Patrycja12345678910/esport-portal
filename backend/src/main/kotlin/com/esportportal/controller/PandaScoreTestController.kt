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

@RestController
class PandaScoreTestController(
    private val pandaScoreClient: PandaScoreClient,
    private val pandaScoreSyncService: PandaScoreSyncService

) {

    @GetMapping("/api/test/pandascore")
    fun testPandaScore(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzNadchodzaceMeczeCS2()
    }

    @GetMapping("/api/test/pandascore/past")
    fun testPastMatches(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzZakonczoneMeczeCS2()
    }

    @PostMapping("/api/test/pandascore/sync")
    fun testSync(): String {
        pandaScoreSyncService.syncMatches()

        return "Synchronizacja zakończona"
    }
        @GetMapping("/api/test/pandascore/incidents")
        fun testIncidents(): List<PandaScoreIncidentDto> {
            val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

            return pandaScoreClient.pobierzZmianyMeczowCS2(odKiedy)
        }
    @GetMapping("/api/test/pandascore/incidents/raw")
    fun testRawIncidents(): String {
        val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

        return pandaScoreClient.pobierzSuroweIncydenty(odKiedy)
    }
    @GetMapping("/api/test/pandascore/additions")
    fun testAdditions(): List<PandaScoreIncidentDto> {
        val odKiedy = Instant.now().minus(24, ChronoUnit.HOURS)

        return pandaScoreClient.pobierzNoweMeczeCS2(odKiedy)
    }
    @GetMapping("/api/test/pandascore/match")
    fun testMatch(): PandaScoreMatchDto? {
        return pandaScoreClient.pobierzMeczPoId(1795395)
    }
    @GetMapping("/api/test/pandascore/changes/check")
    fun testChangesCheck(): Boolean {
        val odKiedy = Instant.parse("2026-11-09T16:00:00Z")

        return pandaScoreClient.pobierzZmianyMeczowCS2(odKiedy)
            .any { it.id == 1795395L }
    }
    @PostMapping("/api/test/pandascore/match/sync")
    fun testSingleMatchSync(): Boolean {
        return pandaScoreSyncService.syncSingleMatch(1795395L)
    }
    }

