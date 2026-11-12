package com.esportportal

import org.springframework.stereotype.Component
import org.springframework.resilience.annotation.Retryable

@Component
class RetrySimulator {

    var liczbaProb = 0

    @Retryable(
        includes = [IllegalStateException::class],
        maxRetries = 2,
        delay = 1000
    )
    fun symulujPobieranie() {
        liczbaProb += 1
        println("Próba numer: $liczbaProb")

        if (liczbaProb < 3) {
            throw IllegalStateException("Wystąpił błąd")
        } else {
            println("Pobrano dane pomyślnie")
        }
    }
}