package com.manu.kode.engrama.navigation

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.junit.Assert.assertEquals
import org.junit.Test

/** Las rutas con argumentos sobreviven a la serialización que usa Navigation. */
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
