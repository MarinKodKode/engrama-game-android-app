package com.manu.kode.engrama.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.data.model.AppSettings
import com.manu.kode.engrama.data.model.Division
import com.manu.kode.engrama.data.store.SessionStore
import com.manu.kode.engrama.data.store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStore: SettingsStore,
    private val sessionStore: SessionStore
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsStore.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    val totalPoints: Int get() = sessionStore.totalPoints
    val currentDivision: Division get() = sessionStore.currentDivision

    val progress: Float get() {
        val current = currentDivision
        val next = current.next ?: return 1f
        val range = next.threshold - current.threshold
        if (range <= 0) return 0f
        val earned = maxOf(totalPoints - current.threshold, 0)
        return minOf(earned.toFloat() / range.toFloat(), 1f)
    }

    val pointsToNext: Int get() {
        val next = currentDivision.next ?: return 0
        return maxOf(next.threshold - totalPoints, 0)
    }

    fun updateName(name: String) {
        settingsStore.updateName(name)
    }

    fun updateHaptic(enabled: Boolean) {
        settingsStore.updateHaptic(enabled)
    }

    fun updateMathTimeLimit(seconds: Int) {
        val updated = settings.value.copy(
            math = settings.value.math.copy(timeLimit = seconds)
        )
        settingsStore.save(updated)
    }

    fun updateSpanishTimeLimit(seconds: Int) {
        val updated = settings.value.copy(
            spanish = settings.value.spanish.copy(timeLimit = seconds)
        )
        settingsStore.save(updated)
    }

    fun updateTriviaTimeLimit(seconds: Int) {
        val updated = settings.value.copy(
            trivia = settings.value.trivia.copy(timeLimit = seconds)
        )
        settingsStore.save(updated)
    }

    fun updateTriviaDifficulty(difficulty: String) {
        val updated = settings.value.copy(
            trivia = settings.value.trivia.copy(difficulty = difficulty)
        )
        settingsStore.save(updated)
    }

    fun updateSpanishCategories(categories: List<String>) {
        val updated = settings.value.copy(
            spanish = settings.value.spanish.copy(activeCategories = categories)
        )
        settingsStore.save(updated)
    }

    fun updateTriviaCategories(categories: List<String>) {
        val updated = settings.value.copy(
            trivia = settings.value.trivia.copy(activeCategories = categories)
        )
        settingsStore.save(updated)
    }
}