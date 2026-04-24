package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.data.model.GameSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val sessions: Flow<List<GameSession>>
    suspend fun addSession(session: GameSession)
    suspend fun clearAll()
}