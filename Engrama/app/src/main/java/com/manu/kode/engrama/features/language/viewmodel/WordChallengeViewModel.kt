package com.manu.kode.engrama.features.language.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.core.haptic.HapticManager
import com.manu.kode.engrama.core.haptic.SoundManager
import com.manu.kode.engrama.data.dataset.DatasetRepository
import com.manu.kode.engrama.data.repository.SessionRepository
import com.manu.kode.engrama.data.repository.SettingsRepository
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameSession
import com.manu.kode.engrama.domain.model.GameType
import com.manu.kode.engrama.domain.model.WordEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordChallengeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val sessionRepository: SessionRepository,
    private val datasetRepository: DatasetRepository,
    private val hapticManager: HapticManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _currentWord = MutableStateFlow<WordEntry?>(null)
    val currentWord: StateFlow<WordEntry?> = _currentWord.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _timeLeft = MutableStateFlow(30)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _wrongSelection = MutableStateFlow(false)
    val wrongSelection: StateFlow<Boolean> = _wrongSelection.asStateFlow()

    private val _bounceCorrect = MutableStateFlow(false)
    val bounceCorrect: StateFlow<Boolean> = _bounceCorrect.asStateFlow()

    private val _activeCategories = MutableStateFlow<List<String>>(emptyList())
    val activeCategories: StateFlow<List<String>> = _activeCategories.asStateFlow()

    private var allWords: List<WordEntry> = emptyList()
    private var usedWords: MutableSet<String> = mutableSetOf()
    private var totalTime: Int = 30
    private var hapticEnabled: Boolean = true
    private var timerJob: Job? = null

    val progress: Float get() = _timeLeft.value.toFloat() / totalTime.toFloat()

    fun init() {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            totalTime = settings.spanish.timeLimit
            hapticEnabled = settings.hapticOnError
            _activeCategories.value = settings.spanish.activeCategories.map { it.rawValue }
            _timeLeft.value = totalTime
            loadWords()
            startCountdown()
        }
    }

    private suspend fun loadWords() {
        allWords = try {
            datasetRepository.words().filter {
                _activeCategories.value.contains(it.category.rawValue)
            }.shuffled()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun startCountdown() {
        viewModelScope.launch {
            delay(2000)
            nextWord()
            startTimer()
        }
    }

    fun nextWord() {
        _selectedCategory.value = null
        _wrongSelection.value = false
        _bounceCorrect.value = false

        if (usedWords.size >= allWords.size) usedWords.clear()

        val word = allWords.firstOrNull { !usedWords.contains(it.word) }
        if (word != null) {
            usedWords.add(word.word)
            _currentWord.value = word
        }
    }

    fun onConfirm() {
        val selected = _selectedCategory.value ?: return
        val current = _currentWord.value ?: return

        if (selected == current.category.rawValue) {
            _score.value++
            hapticManager.success()
            nextWord()
        } else {
            _wrongSelection.value = true
            _bounceCorrect.value = true
            if (hapticEnabled) hapticManager.error()

            viewModelScope.launch {
                delay(1000)
                _wrongSelection.value = false
                _bounceCorrect.value = false
                _selectedCategory.value = null
            }
        }
    }

    fun selectCategory(category: String) {
        if (!_wrongSelection.value) {
            _selectedCategory.value = category
        }
    }

    private fun startTimer() {
        _timeLeft.value = totalTime
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value--
            }
            endGame()
        }
    }

    fun endGame() {
        timerJob?.cancel()
        soundManager.play(SoundManager.GameSound.SOUND_7)

        val session = GameSession(
            gameType = GameType.LANGUAGE,
            operation = GameOperation.TIPO_PALABRA,
            score = _score.value,
            timeLimit = totalTime
        )
        viewModelScope.launch { sessionRepository.add(session) }
        _gameOver.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}