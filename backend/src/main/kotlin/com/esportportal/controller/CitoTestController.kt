package com.esportportal.controller

import com.esportportal.integration.cito.CitoClient
import com.esportportal.integration.cito.dto.CitoTeamDto
import com.esportportal.integration.cito.dto.CitoTeamsResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test/cito")
class CitoTestController(
    private val citoClient: CitoClient
) {
    @GetMapping("/teams")
    fun testCitoTeams() : List<CitoTeamDto> {
        return citoClient.pobierzDruzyny()
    }

}