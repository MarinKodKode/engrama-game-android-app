package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/TriviaSettings.swift:10-14. */
data class TriviaSettings(
    val timeLimit: Int = 60,
    val activeCategories: List<TriviaCategory> = TriviaCategory.entries,
    val difficulty: TriviaDifficulty = TriviaDifficulty.MEDIUM
)
