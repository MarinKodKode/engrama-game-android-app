package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.domain.model.PlayerProgress
import kotlinx.coroutines.flow.Flow

interface PlayerProgressRepository {
    /** Derivado de las sesiones; la división nunca se persiste. */
    val progress: Flow<PlayerProgress>
}
