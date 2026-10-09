package com.manu.kode.engrama.domain.model

import java.time.Instant
import java.util.UUID

/** iOS: Core/Analytics/GameSession.swift:10-28. */
data class GameSession(
    val id: String = UUID.randomUUID().toString(),
    val date: Instant = Instant.now(),
    val gameType: GameType,
    val operation: GameOperation,
    val score: Int,
    val timeLimit: Int,
    val numberOfDigits: Int = 0
)
