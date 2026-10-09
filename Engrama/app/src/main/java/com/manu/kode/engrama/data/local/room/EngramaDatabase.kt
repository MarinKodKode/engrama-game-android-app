package com.manu.kode.engrama.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [GameSessionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EngramaDatabase : RoomDatabase() {
    abstract fun gameSessionDao(): GameSessionDao

    companion object {
        const val NAME = "engrama.db"
    }
}
