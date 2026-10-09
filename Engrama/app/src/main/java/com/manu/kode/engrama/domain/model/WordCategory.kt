package com.manu.kode.engrama.domain.model

/** iOS: Features/Grammar+Language/TypeWordChallenge/Models/WordEntry.swift:34-42. */
enum class WordCategory(override val rawValue: String) : RawValueEnum {
    VERBO("verbo"),
    SUSTANTIVO("sustantivo"),
    ADJETIVO("adjetivo"),
    ADVERBIO("adverbio"),
    ARTICULO("articulo"),
    PRONOMBRE("pronombre"),
    PREPOSICION("preposicion"),
    CONJUNCION("conjuncion")
}
