package com.manu.kode.engrama.data.local.room

import com.manu.kode.engrama.domain.model.GameSession

fun GameSession.toEntity(): GameSessionEntity = GameSessionEntity(
    id = id,
    date = date,
    gameType = gameType,
    operation = operation,
    score = score,
    timeLimit = timeLimit,
    numberOfDigits = numberOfDigits
)

fun GameSessionEntity.toDomain(): GameSession = GameSession(
    id = id,
    date = date,
    gameType = gameType,
    operation = operation,
    score = score,
    timeLimit = timeLimit,
    numberOfDigits = numberOfDigits
)
