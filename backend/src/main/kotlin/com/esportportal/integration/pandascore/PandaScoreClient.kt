package com.esportportal.integration.pandascore

import com.esportportal.integration.pandascore.dto.PandaScoreMatchDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.http.client.JdkClientHttpRequestFactory
import java.time.Duration
import org.springframework.resilience.annotation.Retryable
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.HttpClientErrorException
import com.esportportal.integration.pandascore.dto.PandaScoreIncidentDto
import com.esportportal.integration.pandascore.dto.PandaScoreDeletionDto
import java.time.Instant
import kotlin.contracts.Returns


@Component
class PandaScoreClient(
    restClientBuilder: RestClient.Builder,
    @Value("\${PANDASCORE_API_TOKEN}") private val apiToken: String,
    @Value("\${PANDASCORE_BASE_URL:https://api.pandascore.co}") private val baseUrl: String


) {
    private val requestFactory = JdkClientHttpRequestFactory().apply {
        setReadTimeout(Duration.ofSeconds(5))
    }
    private val restClient = restClientBuilder
        .baseUrl(baseUrl)
        .defaultHeader("Authorization", "Bearer $apiToken")
        .requestFactory(requestFactory)
        .build()

    @Retryable(
        includes = [ResourceAccessException::class,HttpServerErrorException::class],
        maxRetries = 2,
        delay = 2000

    )
    fun pobierzNadchodzaceMeczeCS2(): List<PandaScoreMatchDto> {
        return pobierzMeczeCS2("/csgo/matches/upcoming")
    }
    @Retryable(
        includes = [ResourceAccessException::class,HttpServerErrorException::class],
        maxRetries = 2,
        delay = 2000
    )
    fun pobierzTrwajaceMeczeCS2(): List<PandaScoreMatchDto> {
        return pobierzMeczeCS2("/csgo/matches/running")
    }

    private fun odczytajRetryAfter(e: HttpClientErrorException.TooManyRequests
    ): String? {
        return e.responseHeaders?.getFirst("Retry-After")
    }

    fun zamienNasekundy(tekst: String?): Long? {
        return tekst?.toLongOrNull()?.takeIf { it >= 0 }
    }
    private fun obliczCzasOczekiwania(e: HttpClientErrorException.TooManyRequests): Long?{
        val naglowek = odczytajRetryAfter(e)
        return zamienNasekundy(naglowek)
    }
    private fun sekundyNaMilisekundy(sekundy: Long): Long {
        return sekundy.coerceAtMost(60) * 1000
    }

    @Retryable(
        includes = [ResourceAccessException::class,HttpServerErrorException::class],
        maxRetries = 2,
        delay = 2000
    )
    fun pobierzZakonczoneMeczeCS2() : List<PandaScoreMatchDto> {
        return pobierzMeczeCS2("/csgo/matches/past")
    }

    private fun pobierzMeczeCS2(sciezka: String): List<PandaScoreMatchDto> {
        var liczbaProb = 0

        while (true) {
            liczbaProb += 1

            try {
                return restClient.get()
                    .uri { builder ->
                        builder.path(sciezka)
                            .queryParam("per_page", 5)
                            .queryParam("filter[videogame_title]", "cs-2")
                            .build()
                    }
                    .retrieve()
                    .body(Array<PandaScoreMatchDto>::class.java)
                    ?.toList() ?: emptyList()
            } catch (e: HttpClientErrorException.TooManyRequests) {
                val sekundy = obliczCzasOczekiwania(e)
                if (liczbaProb >= 2) {
                    throw e
                }
                if (sekundy == null || sekundy > 60) {
                    throw e
                }
                println("Czekam $sekundy sekund")
                try {
                    Thread.sleep(sekundyNaMilisekundy(sekundy))
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw e
                }
            }
        }
    }
    @Retryable(
        includes = [ResourceAccessException::class,HttpServerErrorException::class],
        maxRetries = 2,
        delay = 2000
    )
    fun pobierzNoweMeczeCS2(
        odKiedy: Instant,
        strona: Int = 1
    ): List<PandaScoreIncidentDto> {

        return restClient.get()
            .uri { builder ->
                builder.path("/additions")
                    .queryParam("type", "match")
                    .queryParam("videogame", "cs-go")
                    .queryParam("since", odKiedy.toString())
                    .queryParam("page", strona)
                    .queryParam("per_page", 100)
                    .build()
            }
            .retrieve()
            .body(Array<PandaScoreIncidentDto>::class.java)
            ?.toList() ?: emptyList()
    }
    fun pobierzZmianyMeczowCS2(odKiedy: Instant, strona: Int =1) : List<PandaScoreIncidentDto> {
        return restClient.get()
            .uri { builder ->
                builder.path("/changes")
                    .queryParam("type", "match")
                    .queryParam("videogame","cs-go")
                    .queryParam("since",odKiedy.toString())
                    .queryParam("page",strona)
                    .queryParam("per_page",100)
                    .build()
            }
            .retrieve()
            .body(Array<PandaScoreIncidentDto>::class.java)
        ?.toList() ?: emptyList()

    }
    fun pobierzSuroweIncydenty(odKiedy: Instant): String {
        return restClient.get()
            .uri { builder ->
                builder.path("/incidents")
                    .queryParam("type", "match")
                    .queryParam("videogame", "cs-go")
                    .queryParam("since", odKiedy.toString())
                    .queryParam("page", 1)
                    .queryParam("per_page", 5)
                    .build()
            }
            .retrieve()
            .body(String::class.java) ?: "[]"
    }

    fun pobierzUsunieteMeczeCS2(
        odKiedy: Instant,
        strona: Int = 1
    ): List<PandaScoreDeletionDto> {

        return restClient.get()
            .uri { builder ->
                builder.path("/deletions")
                    .queryParam("type", "match")
                    .queryParam("videogame", "cs-go")
                    .queryParam("since", odKiedy.toString())
                    .queryParam("page", strona)
                    .queryParam("per_page", 100)
                    .build()
            }
            .retrieve()
            .body(Array<PandaScoreDeletionDto>::class.java)
            ?.toList() ?: emptyList()
    }
    fun pobierzMeczPoId(id: Long): PandaScoreMatchDto? {
        return restClient.get()
            .uri("/matches/{id}", id)
            .retrieve()
            .body(PandaScoreMatchDto::class.java)
    }
}