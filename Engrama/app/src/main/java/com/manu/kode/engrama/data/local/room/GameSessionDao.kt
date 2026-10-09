package com.manu.kode.engrama.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSessionDao {

    @Query("SELECT * FROM game_sessions ORDER BY date DESC")
    fun observeAll(): Flow<List<GameSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: GameSessionEntity)

    @Query("DELETE FROM game_sessions")
    suspend fun clear()
}
