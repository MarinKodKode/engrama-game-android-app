package com.manu.kode.engrama.data.model

data class SpanishSettings(
    val timeLimit: Int = 30,
    val activeCategories: List<String> = listOf(
        "verbo", "sustantivo", "adjetivo", "adverbio"
    )
)