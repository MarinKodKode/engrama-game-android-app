package com.manu.kode.engrama.navigation

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Las rutas con argumentos sobreviven a la serialización (kotlinx-serialization).
 * No cubre la codificación de URL que hace Navigation en Android (pendiente opcional,
 * engrama-port-notes/12).
 */
class RoutesTest {

    private inline fun <reified T> roundTrip(value: T): T =
        Json.decodeFromString(serializer<T>(), Json.encodeToString(serializer<T>(), value))

    @Test
    fun `ChallengeOperation conserva todos los argumentos con cada signo`() {
        listOf("+", "-", "*", "/").forEach { sign ->
            val route = ChallengeOperation(
                sign = sign, digits = 3, timeLimit = 45, useNegatives = true, hapticEnabled = false
            )
            assertEquals(route, roundTrip(route))
        }
    }

    @Test
    fun `ChallengeOperation conserva signos con caracteres especiales`() {
        listOf("/", "?", "#", "%", "&", " ", "÷", "×", "a/b?c#d%e&f g").forEach { sign ->
            val route = ChallengeOperation(
                sign = sign, digits = 1, timeLimit = 30, useNegatives = false, hapticEnabled = true
            )
            assertEquals("signo '$sign'", route, roundTrip(route))
        }
    }

    @Test
    fun `rutas de resumen conservan el score`() {
        assertEquals(SummaryMath(12), roundTrip(SummaryMath(12)))
        assertEquals(SummaryLanguage(0), roundTrip(SummaryLanguage(0)))
        assertEquals(SummaryTrivia(-3), roundTrip(SummaryTrivia(-3)))
    }

    @Test
    fun `rutas sin argumentos son serializables`() {
        assertEquals(Home, roundTrip(Home))
        assertEquals(ChooseOperation, roundTrip(ChooseOperation))
        assertEquals(Settings, roundTrip(Settings))
        assertEquals(Analytics, roundTrip(Analytics))
        assertEquals(WordChallenge, roundTrip(WordChallenge))
        assertEquals(TriviaChallenge, roundTrip(TriviaChallenge))
    }
}
