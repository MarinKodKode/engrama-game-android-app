package com.manu.kode.engrama.domain.model

/** iOS: Features/Trivia/Models/TriviaQuestion.swift:11-18. */
data class TriviaQuestion(
    val id: String,
    val question: String,
    val answer: String,
    val options: List<String>,
    val category: TriviaCategory,
    val difficulty: TriviaDifficulty
)
