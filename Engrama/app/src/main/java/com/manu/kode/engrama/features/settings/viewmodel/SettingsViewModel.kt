package com.manu.kode.engrama.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.data.repository.PlayerProgressRepository
import com.manu.kode.engrama.data.repository.SettingsRepository
import com.manu.kode.engrama.domain.model.AppSettings
import com.manu.kode.engrama.domain.model.PlayerProgress
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.domain.model.fromRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    playerProgressRepository: PlayerProgressRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    val playerProgress: StateFlow<PlayerProgress> = playerProgressRepository.progress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerProgress.from(0)
        )

    fun updateName(name: String) {
        viewModelScope.launch { settingsRepository.setProfileName(name) }
    }

    fun updateHaptic(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHapticOnError(enabled) }
    }

    fun updateMathTimeLimit(seconds: Int) {
        viewModelScope.launch { settingsRepository.setMathTimeLimit(seconds) }
    }

    fun updateSpanishTimeLimit(seconds: Int) {
        viewModelScope.launch { settingsRepository.setSpanishTimeLimit(seconds) }
    }

    fun updateTriviaTimeLimit(seconds: Int) {
        viewModelScope.launch { settingsRepository.setTriviaTimeLimit(seconds) }
    }

    fun updateTriviaDifficulty(difficulty: String) {
        val value = fromRaw<TriviaDifficulty>(difficulty) ?: return
        viewModelScope.launch { settingsRepository.setTriviaDifficulty(value) }
    }

    fun updateSpanishCategories(categories: List<WordCategory>) {
        viewModelScope.launch { settingsRepository.setSpanishCategories(categories) }
    }

    fun updateTriviaCategories(categories: List<TriviaCategory>) {
        viewModelScope.launch { settingsRepository.setTriviaCategories(categories) }
    }
}
