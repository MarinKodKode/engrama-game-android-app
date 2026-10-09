package com.manu.kode.engrama.domain.model

/**
 * iOS: Features/Grammar+Language/TypeWordChallenge/Models/WordEntry.swift:11-28.
 * id se genera al parsear, como en iOS; difficulty es String porque iOS no tiene enum.
 */
data class WordEntry(
    val id: String,
    val word: String,
    val category: WordCategory,
    val difficulty: String
)
