package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.PlayerProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPlayerProgressRepository @Inject constructor(
    sessionRepository: SessionRepository
) : PlayerProgressRepository {

    override val progress: Flow<PlayerProgress> = sessionRepository.sessions
        .map { sessions -> PlayerProgress.from(sessions.sumOf { it.score }) }
        .distinctUntilChanged()
}
