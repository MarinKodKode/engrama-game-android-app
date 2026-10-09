package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.core.coroutines.ApplicationScope
import com.manu.kode.engrama.data.local.room.GameSessionDao
import com.manu.kode.engrama.data.local.room.toDomain
import com.manu.kode.engrama.data.local.room.toEntity
import com.manu.kode.engrama.domain.model.GameSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomSessionRepository @Inject constructor(
    private val dao: GameSessionDao,
    @param:ApplicationScope private val appScope: CoroutineScope
) : SessionRepository {

    override val sessions: Flow<List<GameSession>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun add(session: GameSession) {
        // El insert corre en el scope de aplicación; si el llamador se cancela,
        // join() lanza CancellationException pero el insert termina igual.
        appScope.launch { dao.insert(session.toEntity()) }.join()
    }

    override suspend fun clear() {
        dao.clear()
    }
}
