package com.manu.kode.engrama.data.model

import java.util.Date
import java.util.UUID

data class GameSession(
    val id: String = UUID.randomUUID().toString(),
    val date: Date = Date(),
    val gameType: GameType,
    val operation: String,
    val score: Int,
    val timeLimit: Int,
    val numberOfDigits: Int = 0
)