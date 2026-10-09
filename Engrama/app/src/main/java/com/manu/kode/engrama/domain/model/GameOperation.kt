package com.manu.kode.engrama.domain.model

/**
 * Valores de GameSession.operation que produce iOS (allí es un String libre):
 * - MathOperator: Features/MathChallenge/ViewModels/ChallengeViewModel.swift:167-172
 * - "tipo_palabra": Features/Grammar+Language/TypeWordChallenge/ViewModels/TypeWordChallengeVIewModel.swift:142
 * - "trivia": Features/Trivia/ViewModels/TriviaViewModel.swift:152
 * - "flashcards_<StudyMode>": Features/Flashcards/ViewModels/StudySessionViewModel.swift:96,
 *   Features/Flashcards/Models/StudyMode.swift:8-11
 */
enum class GameOperation(override val rawValue: String) : RawValueEnum {
    ADD("+"),
    SUBSTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    TIPO_PALABRA("tipo_palabra"),
    TRIVIA("trivia"),
    FLASHCARDS_CLASSIC("flashcards_classic"),
    FLASHCARDS_QUIZ("flashcards_quiz"),
    FLASHCARDS_SPACED_REPETITION("flashcards_spacedRepetition");

    val isMath: Boolean
        get() = this == ADD || this == SUBSTRACT || this == MULTIPLY || this == DIVIDE
}
