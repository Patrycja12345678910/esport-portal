package com.esportportal.integration.pandascore

import org.junit.jupiter.api.Test
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.web.client.RestClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.springframework.web.client.HttpClientErrorException


class PandaScoreClientTest {

    @Test
    fun testObslugiBledu429(){
        val liczbaZapytan = AtomicInteger(0)

        val serwer = HttpServer.create(InetSocketAddress(0), 0)
        serwer.createContext("/csgo/matches/upcoming") { exchange ->
            val numerZapytania = liczbaZapytan.incrementAndGet()

            if (numerZapytania == 1) {
                exchange.responseHeaders.add("Retry-After", "1")
                exchange.sendResponseHeaders(429, -1)
            } else {
                val odpowiedz = "[]".toByteArray()

                exchange.responseHeaders.add("Content-Type", "application/json")
                exchange.sendResponseHeaders(200, odpowiedz.size.toLong())

                exchange.responseBody.use { it.write(odpowiedz) }
            }

            exchange.close()
        }
        serwer.start()
        val adresSerwera = "http://localhost:${serwer.address.port}"

        val klient = PandaScoreClient(
            RestClient.builder(),
            "test-token",
            adresSerwera
        )
        try {
            val mecze = klient.pobierzNadchodzaceMeczeCS2()

            assertTrue(mecze.isEmpty())
            assertEquals(2, liczbaZapytan.get())

        } finally {
            serwer.stop(0)
        }
    }
    @Test
    fun testLimituProb429(){
        val liczbaZapytan = AtomicInteger(0)
        val serwer = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)

        serwer.createContext("/csgo/matches/upcoming") { exchange ->
            liczbaZapytan.incrementAndGet()

            exchange.responseHeaders.add("Retry-After", "1")
            exchange.sendResponseHeaders(429, -1)
            exchange.close()
        }

        serwer.start()

        try {
            val adresSerwera = "http://127.0.0.1:${serwer.address.port}"

            val klient = PandaScoreClient(
                RestClient.builder(),
                "test-token",
                adresSerwera
            )

            assertThrows(HttpClientErrorException.TooManyRequests::class.java) {
                klient.pobierzNadchodzaceMeczeCS2()
            }

            assertEquals(2, liczbaZapytan.get())
        } finally {
            serwer.stop(0)
        }
    }
}