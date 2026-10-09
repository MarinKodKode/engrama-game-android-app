package com.manu.kode.engrama.data.local.room

import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameSession
import com.manu.kode.engrama.domain.model.GameType
import com.manu.kode.engrama.testutil.LogSinkRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class ConvertersTest {

    @get:Rule
    val log = LogSinkRule()

    private val converters = Converters()

    @Test
    fun gameType_roundTrip() {
        GameType.entries.forEach {
            assertEquals(it, converters.rawToGameType(converters.gameTypeToRaw(it)))
        }
        assertEquals("math", converters.gameTypeToRaw(GameType.MATH))
    }

    @Test
    fun gameOperation_roundTrip() {
        GameOperation.entries.forEach {
            assertEquals(it, converters.rawToGameOperation(converters.gameOperationToRaw(it)))
        }
        assertEquals("tipo_palabra", converters.gameOperationToRaw(GameOperation.TIPO_PALABRA))
    }

    @Test
    fun instant_roundTripInMillis() {
        val instant = Instant.ofEpochMilli(1_791_485_779_278)
        assertEquals(1_791_485_779_278, converters.instantToEpochMillis(instant))
        assertEquals(instant, converters.epochMillisToInstant(converters.instantToEpochMillis(instant)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownGameType_throws() {
        converters.rawToGameType("MATH")
    }

    @Test(expected = IllegalArgumentException::class)
    fun unknownGameOperation_throws() {
        converters.rawToGameOperation("suma")
    }

    @Test
    fun mapper_roundTrip() {
        val session = GameSession(
            id = "id-1",
            date = Instant.ofEpochMilli(1_000),
            gameType = GameType.TRIVIA,
            operation = GameOperation.TRIVIA,
            score = 7,
            timeLimit = 60,
            numberOfDigits = 0
        )
        assertEquals(session, session.toEntity().toDomain())
    }
}
