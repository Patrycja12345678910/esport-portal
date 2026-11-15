package com.esportportal.integration.cito

import com.esportportal.integration.cito.dto.CitoTeamDto
import com.esportportal.integration.cito.dto.CitoTeamsResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient


@Component
class CitoClient (
    restClientBuilder: RestClient.Builder,
    @Value("\${cito.api.key}") private val apiKey: String,
    @Value("\${cito.api.base-url}") private val baseUrl: String
) {
    private val restClient = restClientBuilder
        .baseUrl(baseUrl)
        .defaultHeader("x-api-key", apiKey)
        .build()


    fun pobierzDruzyny(strona: Int = 1): List<CitoTeamDto> {
        val response = restClient.get()
            .uri("/cs2/teams?page=$strona&limit=10")
            .retrieve()
            .body(CitoTeamsResponseDto::class.java)

        return response?.data
            ?: throw IllegalStateException("Cito API nie zwróciło odpowiedzi")

    }
}
