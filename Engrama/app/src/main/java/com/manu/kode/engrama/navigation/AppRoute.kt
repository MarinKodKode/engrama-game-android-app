package com.manu.kode.engrama.navigation

sealed class AppRoute(val route: String) {
    object Home : AppRoute("home")
    object ChooseOperation : AppRoute("choose_operation")
    object Settings : AppRoute("settings")
    object Analytics : AppRoute("analytics")

    data class ChallengeOperation(
        val sign: String,
        val digits: Int,
        val timeLimit: Int,
        val useNegatives: Boolean,
        val hapticEnabled: Boolean
    ) : AppRoute(
        "challenge_operation/{sign}/{digits}/{timeLimit}/{useNegatives}/{hapticEnabled}"
    ) {
        companion object {
            const val ROUTE = "challenge_operation/{sign}/{digits}/{timeLimit}/{useNegatives}/{hapticEnabled}"
            fun createRoute(
                sign: String,
                digits: Int,
                timeLimit: Int,
                useNegatives: Boolean,
                hapticEnabled: Boolean
            ) = "challenge_operation/$sign/$digits/$timeLimit/$useNegatives/$hapticEnabled"
        }
    }

    data class SummaryMath(val score: Int) : AppRoute("summary_math/{score}") {
        companion object {
            const val ROUTE = "summary_math/{score}"
            fun createRoute(score: Int) = "summary_math/$score"
        }
    }

    object WordChallenge : AppRoute("word_challenge")

    data class SummaryLanguage(val score: Int) : AppRoute("summary_language/{score}") {
        companion object {
            const val ROUTE = "summary_language/{score}"
            fun createRoute(score: Int) = "summary_language/$score"
        }
    }

    object TriviaChallenge : AppRoute("trivia_challenge")

    data class SummaryTrivia(val score: Int) : AppRoute("summary_trivia/{score}") {
        companion object {
            const val ROUTE = "summary_trivia/{score}"
            fun createRoute(score: Int) = "summary_trivia/$score"
        }
    }
}