package com.manu.kode.engrama.data.model

data class TriviaSettings(
    val timeLimit: Int = 60,
    val activeCategories: List<String> = listOf(
        "quimica", "fisica", "biologia", "geografia",
        "español", "matematicas", "historia", "miscelaneos"
    ),
    val difficulty: String = "medium"
)