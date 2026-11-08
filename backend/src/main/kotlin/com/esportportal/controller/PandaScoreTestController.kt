package com.esportportal.controller

import com.esportportal.integration.pandascore.PandaScoreClient
import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.esportportal.integration.pandascore.PandaScoreSyncService
import org.springframework.web.bind.annotation.PostMapping

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
    fun testSync(): String{
        pandaScoreSyncService.syncMatches()

        return "Synchronizacja zakończona"
    }


}