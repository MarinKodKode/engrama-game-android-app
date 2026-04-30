package com.manu.kode.engrama.data.repository

import com.manu.kode.engrama.data.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun save(settings: AppSettings)
}