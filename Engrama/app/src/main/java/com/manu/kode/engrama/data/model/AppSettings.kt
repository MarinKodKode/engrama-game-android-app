package com.manu.kode.engrama.data.model

data class AppSettings(
    val math: MathSettings = MathSettings(),
    val spanish: SpanishSettings = SpanishSettings(),
    val trivia: TriviaSettings = TriviaSettings(),
    val profile: UserProfile = UserProfile(),
    val hapticOnError: Boolean = true
)