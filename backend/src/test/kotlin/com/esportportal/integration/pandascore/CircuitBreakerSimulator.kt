package com.esportportal.integration.pandascore

import org.springframework.stereotype.Component
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.resilience.annotation.Retryable

@Component
class CircuitBreakerSimulator {

    var liczbaProb = 0

    @CircuitBreaker(name = "pandascore")
    fun symulujAwaria() {
        liczbaProb++
        throw IllegalStateException("PandaScore nie odpowiada")
    }

    @Retryable(
        includes = [IllegalStateException::class],
        maxRetries = 2,
        delay = 100
    )
    @CircuitBreaker(name = "pandascore")
    fun symulujAwariaZRetry() {
        liczbaProb++
        throw IllegalStateException("PandaScore nie odpowiada")
    }
}