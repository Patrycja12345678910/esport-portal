package com.esportportal.integration.pandascore

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    properties = [
        "PANDASCORE_API_TOKEN=test-token",
        "spring.task.scheduling.enabled=false"
    ]
)
@ActiveProfiles("test")
class CircuitBreakerTest(
    @Autowired private val simulator: CircuitBreakerSimulator,
    @Autowired private val registry: CircuitBreakerRegistry
) {

    @Test
    fun testBlokowaniaPoAwariach() {

        val breaker = registry.circuitBreaker("pandascore")
        breaker.reset()
        simulator.liczbaProb = 0

        assertEquals(5, breaker.circuitBreakerConfig.minimumNumberOfCalls)

        repeat(5) {
            assertThrows(IllegalStateException::class.java) {
                simulator.symulujAwaria()
            }
        }

        assertThrows(CallNotPermittedException::class.java) {
            simulator.symulujAwaria()
        }

        assertEquals(5, simulator.liczbaProb)
    }

    @Test
    fun testCircuitBreakerZRetry() {

        val breaker = registry.circuitBreaker("pandascore")
        breaker.reset()

        simulator.liczbaProb = 0

        assertThrows(IllegalStateException::class.java) {
            simulator.symulujAwariaZRetry()
            assertEquals(3, simulator.liczbaProb)
        }

        println("Liczba prób: ${simulator.liczbaProb}")
        println("Liczba nieudanych wywołań circuit breakera: ${breaker.metrics.numberOfFailedCalls}")
    }
}