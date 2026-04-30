package com.manu.kode.engrama.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.manu.kode.engrama.core.haptic.HapticManager
import com.manu.kode.engrama.core.haptic.SoundManager
import com.manu.kode.engrama.data.model.Division
import com.manu.kode.engrama.data.repository.DivisionDeserializer
import com.manu.kode.engrama.data.repository.LocalSessionRepository
import com.manu.kode.engrama.data.repository.LocalSettingsRepository
import com.manu.kode.engrama.data.repository.SessionRepository
import com.manu.kode.engrama.data.repository.SettingsRepository
import com.manu.kode.engrama.data.store.SessionStore
import com.manu.kode.engrama.data.store.SettingsStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: LocalSettingsRepository
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        impl: LocalSessionRepository
    ): SessionRepository
}

@Provides
@Singleton
fun provideGson(): Gson = GsonBuilder()
    .registerTypeAdapter(Division::class.java, DivisionDeserializer())
    .create()

@Provides
@Singleton
fun provideSettingsStore(repository: SettingsRepository): SettingsStore {
    return SettingsStore(repository)
}

@Provides
@Singleton
fun provideSessionStore(
    sessionRepository: SessionRepository,
    settingsRepository: SettingsRepository,
    settingsStore: SettingsStore
): SessionStore {
    return SessionStore(sessionRepository, settingsRepository, settingsStore)
}

@Provides
@Singleton
fun provideHapticManager(
    @ApplicationContext context: Context
): HapticManager = HapticManager(context)

@Provides
@Singleton
fun provideSoundManager(
    @ApplicationContext context: Context
): SoundManager = SoundManager(context)