package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.data.local.room.GameSessionDao
import com.manu.kode.engrama.data.local.room.GameSessionEntity
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameSession
import com.manu.kode.engrama.domain.model.GameType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class RoomSessionRepositoryTest {

    /** DAO falso con un insert lento, para poder cancelar al llamador a mitad de la escritura. */
    private class SlowFakeDao(private val insertDelayMs: Long) : GameSessionDao {
        val rows = MutableStateFlow<List<GameSessionEntity>>(emptyList())

        override fun observeAll(): Flow<List<GameSessionEntity>> = rows

        override suspend fun insert(entity: GameSessionEntity) {
            delay(insertDelayMs)
            rows.value = rows.value + entity
        }

        override suspend fun clear() {
            rows.value = emptyList()
        }
    }

    private val session = GameSession(
        id = "s-1",
        date = Instant.ofEpochMilli(1_000),
        gameType = GameType.MATH,
        operation = GameOperation.DIVIDE,
        score = 12,
        timeLimit = 30,
        numberOfDigits = 2
    )

    @Test
    fun add_survivesCancellationOfCallerScope() = runTest {
        val dao = SlowFakeDao(insertDelayMs = 1_000)
        val appScope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler))
        val repo = RoomSessionRepository(dao, appScope)

        // Simula viewModelScope: el ViewModel se destruye mientras el insert está en curso.
        val callerScope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler))
        val caller = callerScope.launch { repo.add(session) }
        runCurrent()
        callerScope.cancel()
        advanceUntilIdle()

        assertTrue(caller.isCancelled)
        assertEquals(listOf("s-1"), dao.rows.value.map { it.id })
    }

    @Test
    fun control_insertInCallerScopeIsLostOnCancellation() = runTest {
        // Sin el scope de aplicación, la misma cancelación pierde la sesión.
        val dao = SlowFakeDao(insertDelayMs = 1_000)
        val callerScope = CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler))
        callerScope.launch { dao.insert(GameSessionEntity("s-1", Instant.EPOCH, GameType.MATH, GameOperation.ADD, 1, 30, 1)) }
        runCurrent()
        callerScope.cancel()
        advanceUntilIdle()

        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun sessions_areMappedToDomain() = runTest {
        val dao = SlowFakeDao(insertDelayMs = 0)
        val repo = RoomSessionRepository(dao, CoroutineScope(SupervisorJob() + StandardTestDispatcher(testScheduler)))

        repo.add(session)

        assertEquals(listOf(session), repo.sessions.first())
    }
}
