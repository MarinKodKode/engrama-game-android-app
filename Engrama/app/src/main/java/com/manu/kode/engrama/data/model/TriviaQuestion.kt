package com.manu.kode.engrama.data.model

data class TriviaQuestion(
    val id: String,
    val question: String,
    val answer: String,
    val options: List<String>,
    val category: String,
    val difficulty: String
)

data class TriviaBank(
    val questions: List<TriviaQuestion>
)

enum class TriviaCategory(val label: String) {
    QUIMICA("Química"),
    FISICA("Física"),
    BIOLOGIA("Biología"),
    GEOGRAFIA("Geografía"),
    ESPANOL("Español"),
    MATEMATICAS("Matemáticas"),
    HISTORIA("Historia"),
    MISCELANEOS("Misceláneos");

    companion object {
        fun fromString(value: String): TriviaCategory? =
            entries.firstOrNull { it.name.lowercase() == value.lowercase() }
    }
}