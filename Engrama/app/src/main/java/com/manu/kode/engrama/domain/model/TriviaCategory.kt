package com.manu.kode.engrama.domain.model

/** iOS: Features/Trivia/Models/TriviaQuestion.swift:20-28. */
enum class TriviaCategory(override val rawValue: String) : RawValueEnum {
    QUIMICA("quimica"),
    FISICA("fisica"),
    BIOLOGIA("biologia"),
    GEOGRAFIA("geografia"),
    ESPANOL("español"),
    MATEMATICAS("matematicas"),
    HISTORIA("historia"),
    MISCELANEOS("miscelaneos")
}
