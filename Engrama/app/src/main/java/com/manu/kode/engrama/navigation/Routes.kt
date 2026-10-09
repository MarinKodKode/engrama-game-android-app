package com.manu.kode.engrama.navigation

import kotlinx.serialization.Serializable

// Rutas tipadas de Navigation Compose (kotlinx-serialization).

@Serializable
data object Home

@Serializable
data object ChooseOperation

@Serializable
data object Settings

@Serializable
data object Analytics

@Serializable
data class ChallengeOperation(
    val sign: String,
    val digits: Int,
    val timeLimit: Int,
    val useNegatives: Boolean,
    val hapticEnabled: Boolean
)

@Serializable
data class SummaryMath(val score: Int)

@Serializable
data object WordChallenge

@Serializable
data class SummaryLanguage(val score: Int)

@Serializable
data object TriviaChallenge

@Serializable
data class SummaryTrivia(val score: Int)
