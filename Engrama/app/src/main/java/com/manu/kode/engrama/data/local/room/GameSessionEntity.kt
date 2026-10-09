package com.manu.kode.engrama.data.local.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameType
import java.time.Instant

/** Columnas con los nombres de iOS (Core/Analytics/GameSession.swift:12-18). */
@Entity(
    tableName = "game_sessions",
    indices = [Index(value = ["date"])]
)
data class GameSessionEntity(
    @PrimaryKey val id: String,
    val date: Instant,
    val gameType: GameType,
    val operation: GameOperation,
    val score: Int,
    val timeLimit: Int,
    val numberOfDigits: Int
)
