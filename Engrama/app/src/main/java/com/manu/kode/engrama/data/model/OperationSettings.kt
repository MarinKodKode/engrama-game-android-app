package com.manu.kode.engrama.data.model

data class OperationSettings(
    val numberOfDigits: Int = 1,
    val useNegativeNumbers: Boolean = false,
    val useDecimalNumbers: Boolean = false
)