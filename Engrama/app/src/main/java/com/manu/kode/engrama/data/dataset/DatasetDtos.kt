package com.manu.kode.engrama.data.dataset

import kotlinx.serialization.Serializable

/** Forma de assets/words.json (idéntico a iOS Resources/JSONData/words.json). */
@Serializable
data class WordBankDto(val words: List<WordEntryDto>)

@Serializable
data class WordEntryDto(
    val word: String,
    val category: String,
    val difficulty: String
)

/** Forma de assets/trivia.json (idéntico a iOS Resources/JSONData/trivia.json). */
@Serializable
data class TriviaBankDto(val questions: List<TriviaQuestionDto>)

@Serializable
data class TriviaQuestionDto(
    val id: String,
    val question: String,
    val answer: String,
    val options: List<String>,
    val category: String,
    val difficulty: String
)
