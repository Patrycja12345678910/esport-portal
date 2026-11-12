package com.esportportal

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Test

@SpringBootTest(properties = ["PANDASCORE_API_TOKEN=test-token"])
class RetryTest(
    @Autowired private val retrySimulator: RetrySimulator
) {

    @Test
    fun testPonawiania() {
        retrySimulator.symulujPobieranie()
        assertEquals(3, retrySimulator.liczbaProb)
    }
}