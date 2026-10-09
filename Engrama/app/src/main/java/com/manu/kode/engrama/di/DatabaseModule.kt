package com.manu.kode.engrama.di

import android.content.Context
import androidx.room.Room
import com.manu.kode.engrama.data.local.room.EngramaDatabase
import com.manu.kode.engrama.data.local.room.GameSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EngramaDatabase =
        Room.databaseBuilder(context, EngramaDatabase::class.java, EngramaDatabase.NAME).build()

    @Provides
    fun provideGameSessionDao(database: EngramaDatabase): GameSessionDao =
        database.gameSessionDao()
}
