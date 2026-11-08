package com.esportportal.integration.pandascore

import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class PandaScoreClient(
    restClientBuilder: RestClient.Builder,
    @Value("\${PANDASCORE_API_TOKEN}") private val apiToken: String
) {
    private val restClient = restClientBuilder
        .baseUrl("https://api.pandascore.co")
        .defaultHeader("Authorization", "Bearer $apiToken")
        .build()

    fun pobierzNadchodzaceMeczeCS2(): List<PandaScoreMatchDto> {
        return restClient.get()
            .uri { builder ->
                builder.path("/csgo/matches/upcoming")
                    .queryParam("per_page", 5)
                    .queryParam("filter[videogame_title]", "cs-2")
                    .build()
            }
            .retrieve()
            .body(Array<PandaScoreMatchDto>::class.java)
            ?.toList() ?: emptyList()
    }
    fun pobierzZakonczoneMeczeCS2() : List<PandaScoreMatchDto> {
        return restClient.get()
            .uri { builder ->
                builder.path("/csgo/matches/past")
                    .queryParam("per_page", 5)
                    .queryParam("filter[videogame_title]", "cs-2")
                    .build()
            }
            .retrieve()
            .body(Array<PandaScoreMatchDto>::class.java)
        ?.toList() ?: emptyList()
    }
}