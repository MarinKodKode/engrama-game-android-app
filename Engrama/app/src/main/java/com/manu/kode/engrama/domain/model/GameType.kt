package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/AppSettings.swift:62-67. */
enum class GameType(override val rawValue: String) : RawValueEnum {
    MATH("math"),
    LANGUAGE("language"),
    TRIVIA("trivia"),
    FLASHCARDS("flashcards")
}
