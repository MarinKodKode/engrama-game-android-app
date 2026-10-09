package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.GameSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSessionRepository : SessionRepository {
    override val sessions: StateFlow<List<GameSession>> get() = state
    val state = MutableStateFlow<List<GameSession>>(emptyList())

    override suspend fun add(session: GameSession) {
        state.value = listOf(session) + state.value
    }

    override suspend fun clear() {
        state.value = emptyList()
    }
}
