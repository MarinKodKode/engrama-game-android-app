package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/SpanishSettings.swift:10-13. */
data class SpanishSettings(
    val timeLimit: Int = 30,
    val activeCategories: List<WordCategory> = DEFAULT_ACTIVE_CATEGORIES
) {
    companion object {
        val DEFAULT_ACTIVE_CATEGORIES = listOf(
            WordCategory.VERBO,
            WordCategory.SUSTANTIVO,
            WordCategory.ADJETIVO,
            WordCategory.ADVERBIO
        )
    }
}
