package com.esportportal.controller

import com.esportportal.integration.pandascore.PandaScoreClient
import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PandaScoreTestController(
    private val pandaScoreClient: PandaScoreClient
) {

    @GetMapping("/api/test/pandascore")
    fun testPandaScore(): List<PandaScoreMatchDto> {
        return pandaScoreClient.pobierzNadchodzaceMeczeCS2()
    }
}