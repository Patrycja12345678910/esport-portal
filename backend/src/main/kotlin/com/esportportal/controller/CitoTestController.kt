package com.esportportal.controller

import com.esportportal.integration.cito.CitoClient
import com.esportportal.integration.cito.dto.CitoTeamDto
import com.esportportal.integration.cito.dto.CitoTeamsResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.esportportal.integration.cito.CitoSyncService
import org.springframework.web.bind.annotation.PostMapping
import com.esportportal.integration.TeamMatchingService
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/test/cito")
class CitoTestController(
    private val citoClient: CitoClient,
    private val citoSyncService: CitoSyncService,
    private val teamMatchingService: TeamMatchingService
) {
    @GetMapping("/teams")
    fun testCitoTeams() : List<CitoTeamDto> {
        return citoClient.pobierzDruzyny().data
    }

    @GetMapping("/matching")
    fun testDopasowania() : String {
        citoSyncService.sprawdzDopasowania()
        return "Sprawdź logi backendu"
    }

    @PostMapping("/sync")
    fun testSynchronizacji() : String {
        citoSyncService.synchronizujDruzyny()
        return "Synchronizacja zakończona"
    }

    @PostMapping("/approve")
    fun testZatwierdzania(
        @RequestParam externalId: String,
        @RequestParam teamId: Long
    ): String {
        teamMatchingService.zatwierdzMapowanieCito(externalId, teamId)
        return "Mapowanie zatwierdzone"
    }

}