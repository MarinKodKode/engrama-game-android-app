package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.GameSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    /** Sesiones ordenadas por fecha descendente. */
    val sessions: Flow<List<GameSession>>

    /** Se guarda en el scope de aplicación: sobrevive a la cancelación del llamador. */
    suspend fun add(session: GameSession)

    suspend fun clear()
}
