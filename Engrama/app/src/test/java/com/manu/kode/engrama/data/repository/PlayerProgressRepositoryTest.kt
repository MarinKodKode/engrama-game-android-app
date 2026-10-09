package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.Division
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameSession
import com.manu.kode.engrama.domain.model.GameType
import com.manu.kode.engrama.domain.model.PlayerProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerProgressRepositoryTest {

    private fun session(score: Int) = GameSession(
        gameType = GameType.MATH,
        operation = GameOperation.ADD,
        score = score,
        timeLimit = 30,
        numberOfDigits = 1
    )

    @Test
    fun noSessions_isPlutonWithZeroPoints() = runTest {
        val repo = DefaultPlayerProgressRepository(FakeSessionRepository())
        assertEquals(PlayerProgress.from(0), repo.progress.first())
        assertEquals(Division.PLUTON, repo.progress.first().division)
    }

    @Test
    fun progress_isDerivedFromSessionsFlow() = runTest {
        val sessions = FakeSessionRepository()
        val repo = DefaultPlayerProgressRepository(sessions)
        val emitted = mutableListOf<PlayerProgress>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.progress.toList(emitted)
        }

        sessions.add(session(60))
        sessions.add(session(45)) // 105 → cruza a Mercurio
        sessions.add(session(0))  // mismo total: no se emite de nuevo

        assertEquals(listOf(0, 60, 105), emitted.map { it.totalPoints })
        assertEquals(Division.MERCURIO, emitted.last().division)
        assertEquals(195, emitted.last().pointsToNext)
    }
}
