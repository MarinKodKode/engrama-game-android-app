package com.manu.kode.engrama.di

import com.manu.kode.engrama.data.dataset.AssetDatasetRepository
import com.manu.kode.engrama.data.dataset.DatasetRepository
import com.manu.kode.engrama.data.local.datastore.DataStoreSettingsRepository
import com.manu.kode.engrama.data.repository.DefaultPlayerProgressRepository
import com.manu.kode.engrama.data.repository.PlayerProgressRepository
import com.manu.kode.engrama.data.repository.RoomSessionRepository
import com.manu.kode.engrama.data.repository.SessionRepository
import com.manu.kode.engrama.data.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSettingsRepository(impl: DataStoreSettingsRepository): SettingsRepository

    @Binds
    abstract fun bindSessionRepository(impl: RoomSessionRepository): SessionRepository

    @Binds
    abstract fun bindPlayerProgressRepository(impl: DefaultPlayerProgressRepository): PlayerProgressRepository

    @Binds
    abstract fun bindDatasetRepository(impl: AssetDatasetRepository): DatasetRepository
}
