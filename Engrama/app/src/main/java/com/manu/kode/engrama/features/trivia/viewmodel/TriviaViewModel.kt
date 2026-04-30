package com.manu.kode.engrama.features.trivia.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manu.kode.engrama.core.haptic.HapticManager
import com.manu.kode.engrama.core.haptic.SoundManager
import com.manu.kode.engrama.data.model.GameSession
import com.manu.kode.engrama.data.model.GameType
import com.manu.kode.engrama.data.model.TriviaBank
import com.manu.kode.engrama.data.model.TriviaQuestion
import com.manu.kode.engrama.data.store.SessionStore
import com.manu.kode.engrama.data.store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TriviaViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsStore: SettingsStore,
    private val sessionStore: SessionStore,
    private val hapticManager: HapticManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<TriviaQuestion?>(null)
    val currentQuestion: StateFlow<TriviaQuestion?> = _currentQuestion.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _timeLeft = MutableStateFlow(60)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption.asStateFlow()

    private val _wrongSelection = MutableStateFlow(false)
    val wrongSelection: StateFlow<Boolean> = _wrongSelection.asStateFlow()

    private val _bounceCorrect = MutableStateFlow(false)
    val bounceCorrect: StateFlow<Boolean> = _bounceCorrect.asStateFlow()

    private var allQuestions: List<TriviaQuestion> = emptyList()
    private var usedIds: MutableSet<String> = mutableSetOf()
    private var totalTime: Int = 60
    private var hapticEnabled: Boolean = true
    private var timerJob: Job? = null

    val progress: Float get() = _timeLeft.value.toFloat() / totalTime.toFloat()

    fun init() {
        val settings = settingsStore.settings.value
        totalTime = settings.trivia.timeLimit
        hapticEnabled = settings.hapticOnError
        _timeLeft.value = totalTime
        loadQuestions(
            activeCategories = settings.trivia.activeCategories,
            difficulty = settings.trivia.difficulty
        )
        startCountdown()
    }

    private fun loadQuestions(activeCategories: List<String>, difficulty: String) {
        try {
            val json = context.assets.open("trivia.json")
                .bufferedReader()
                .use { it.readText() }
            val bank: TriviaBank = Gson().fromJson(json, TriviaBank::class.java)
            allQuestions = bank.questions
                .filter {
                    activeCategories.contains(it.category) &&
                            it.difficulty == difficulty
                }
                .shuffled()
        } catch (e: Exception) {
            allQuestions = emptyList()
        }
    }

    fun startCountdown() {
        viewModelScope.launch {
            delay(2000)
            nextQuestion()
            startTimer()
        }
    }

    fun nextQuestion() {
        _selectedOption.value = null
        _wrongSelection.value = false
        _bounceCorrect.value = false

        if (usedIds.size >= allQuestions.size) usedIds.clear()

        val question = allQuestions
            .firstOrNull { !usedIds.contains(it.id) }
            ?.copy(options = allQuestions
                .firstOrNull { !usedIds.contains(it.id) }!!
                .options.shuffled()
            )

        if (question != null) {
            usedIds.add(question.id)
            _currentQuestion.value = question
        }
    }

    fun onSelect(option: String) {
        if (_wrongSelection.value || _selectedOption.value != null) return
        val current = _currentQuestion.value ?: return

        _selectedOption.value = option

        if (option == current.answer) {
            _score.value++
            hapticManager.success()
            viewModelScope.launch {
                delay(500)
                nextQuestion()
            }
        } else {
            _wrongSelection.value = true
            _bounceCorrect.value = true
            if (hapticEnabled) hapticManager.error()

            viewModelScope.launch {
                delay(1200)
                _wrongSelection.value = false
                _bounceCorrect.value = false
                _selectedOption.value = null
            }
        }
    }

    fun buttonState(option: String): TriviaButtonState {
        val current = _currentQuestion.value ?: return TriviaButtonState.NORMAL
        return when {
            _wrongSelection.value && option == current.answer -> TriviaButtonState.CORRECT
            _wrongSelection.value && option == _selectedOption.value -> TriviaButtonState.WRONG
            _selectedOption.value == option -> TriviaButtonState.SELECTED
            else -> TriviaButtonState.NORMAL
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
            gameType = GameType.TRIVIA,
            operation = "trivia",
            score = _score.value,
            timeLimit = totalTime
        )
        sessionStore.addSession(session)
        _gameOver.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

enum class TriviaButtonState {
    NORMAL, SELECTED, CORRECT, WRONG
}