package com.esportportal.integration.cito

import com.esportportal.integration.cito.dto.CitoTeamsResponseDto
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import java.time.Duration
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.HttpServerErrorException


@Component
class CitoClient(
    restClientBuilder: RestClient.Builder,
    private val citoRateLimiter: CitoRateLimiter,
    @Value("\${cito.api.key}") private val apiKey: String,
    @Value("\${cito.api.base-url}") private val baseUrl: String
) {

    private val requestFactory = JdkClientHttpRequestFactory().apply {
        setReadTimeout(Duration.ofSeconds(5))
    }

    private val restClient = restClientBuilder
        .baseUrl(baseUrl)
        .defaultHeader("x-api-key", apiKey)
        .requestFactory(requestFactory)
        .requestInterceptor { request, body, execution ->
            citoRateLimiter.czekajNaMozliwoscZapytania()
            execution.execute(request, body)
        }
        .build()

    @CircuitBreaker(name = "cito")
    @Retryable(
        includes = [ResourceAccessException::class, HttpServerErrorException::class],
        maxRetries = 2,
        delay = 2000

    )
    fun pobierzDruzyny(strona: Int = 1, limit: Int = 10): CitoTeamsResponseDto {

        var liczbaProb = 0

        while (true) {
            liczbaProb += 1

            try {
                val response = restClient.get()
                    .uri("/cs2/teams?page=$strona&limit=$limit")
                    .retrieve()
                    .body(CitoTeamsResponseDto::class.java)

                return response
                    ?: throw IllegalStateException("Cito API nie zwróciło odpowiedzi")

            } catch (e: HttpClientErrorException.TooManyRequests) {
                val sekundy = odczytajRetryAfter(e)

                if (liczbaProb >= 2) {
                    throw e
                }


                if (sekundy == null || sekundy > 60) {
                    throw e
                }

                println("Przekroczono limit Cito API. Retry- after $sekundy")

                try {
                    Thread.sleep(sekundy * 1000)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw e
                }


            }
        }
    }

    private fun odczytajRetryAfter(
        e: HttpClientErrorException.TooManyRequests
    ): Long? {
        return e.responseHeaders?.getFirst("Retry-After")
            ?.toLongOrNull()?.takeIf { it >= 0 }
    }
}
