package com.manu.kode.engrama.data.model

data class MathSettings(
    val timeLimit: Int = 30,
    val add: OperationSettings = OperationSettings(),
    val subtract: OperationSettings = OperationSettings(),
    val multiply: OperationSettings = OperationSettings(),
    val divide: OperationSettings = OperationSettings()
)