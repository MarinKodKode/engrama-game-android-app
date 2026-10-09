package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/MathSettings.swift:11-17 ("substract" es el nombre de iOS). */
data class MathSettings(
    val timeLimit: Int = 30,
    val add: OperationSettings = OperationSettings(),
    val substract: OperationSettings = OperationSettings(),
    val multiply: OperationSettings = OperationSettings(),
    val divide: OperationSettings = OperationSettings()
)
