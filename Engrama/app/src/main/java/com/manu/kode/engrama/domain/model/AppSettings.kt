package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/AppSettings.swift:35-59. */
data class AppSettings(
    val math: MathSettings = MathSettings(),
    val spanish: SpanishSettings = SpanishSettings(),
    val trivia: TriviaSettings = TriviaSettings(),
    val profile: UserProfile = UserProfile(),
    val hapticOnError: Boolean = true,
    val hapticOnSound: Boolean = true,
    val appearance: AppearanceMode = AppearanceMode.SYSTEM
)
