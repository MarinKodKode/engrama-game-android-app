package com.manu.kode.engrama.features.math.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.core.haptic.HapticManager
import com.manu.kode.engrama.core.haptic.SoundManager
import com.manu.kode.engrama.data.model.GameSession
import com.manu.kode.engrama.data.model.GameType
import com.manu.kode.engrama.data.store.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeOperationViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val hapticManager: HapticManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _numberA = MutableStateFlow(0)
    val numberA: StateFlow<Int> = _numberA.asStateFlow()

    private val _numberB = MutableStateFlow(0)
    val numberB: StateFlow<Int> = _numberB.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _timeLeft = MutableStateFlow(30)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private val _showCountdown = MutableStateFlow(true)
    val showCountdown: StateFlow<Boolean> = _showCountdown.asStateFlow()

    var sign: String = "+"
    var digits: Int = 1
    var totalTime: Int = 30
    var useNegatives: Boolean = false
    var hapticEnabled: Boolean = true

    val progress: Float get() = _timeLeft.value.toFloat() / totalTime.toFloat()

    private var timerJob: Job? = null

    fun init(
        sign: String,
        digits: Int,
        timeLimit: Int,
        useNegatives: Boolean,
        hapticEnabled: Boolean
    ) {
        this.sign = sign
        this.digits = digits
        this.totalTime = timeLimit
        this.useNegatives = useNegatives
        this.hapticEnabled = hapticEnabled
        _timeLeft.value = timeLimit
        randomizeAnswer()
    }

    fun updateResult(value: String) {
        _result.value = value
    }

    fun toggleSign() {
        _result.value = if (_result.value.startsWith("-")) {
            _result.value.removePrefix("-")
        } else {
            "-" + _result.value
        }
    }

    fun randomizeAnswer() {
        val range = when (digits) {
            1 -> 0..9
            2 -> 0..99
            3 -> 0..999
            else -> 0..99
        }

        if (useNegatives) {
            val negRange = -range.last..range.last
            _numberA.value = negRange.random()
            _numberB.value = negRange.random()
        } else {
            _numberA.value = range.random()
            _numberB.value = range.random()
        }

        if (sign == "/" && _numberB.value == 0) {
            _numberB.value = (1..range.last).random()
        }
    }

    fun operate(): Double {
        return when (sign) {
            "+" -> (_numberA.value + _numberB.value).toDouble()
            "-" -> (_numberA.value - _numberB.value).toDouble()
            "*" -> (_numberA.value * _numberB.value).toDouble()
            "/" -> if (_numberB.value != 0) _numberA.value.toDouble() / _numberB.value.toDouble() else 0.0
            else -> 0.0
        }
    }

    fun onTapButton() {
        val preliminar = operate().toInt().toString()
        if (_result.value == preliminar) {
            _score.value++
            _result.value = ""
            hapticManager.success()
            randomizeAnswer()
        } else if (_result.value.isNotEmpty()) {
            if (hapticEnabled) hapticManager.error()
        }
    }

    fun startCountdown() {
        viewModelScope.launch {
            delay(2000)
            _showCountdown.value = false
            startTimer()
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
            gameType = GameType.MATH,
            operation = sign,
            score = _score.value,
            timeLimit = totalTime,
            numberOfDigits = digits
        )
        sessionStore.addSession(session)
        _gameOver.value = true
    }

    val resultPhrase: String get() = when (_score.value) {
        in 0..5   -> "¿Seguro que querías jugar matemáticas?"
        in 6..10  -> "¡Puedes hacerlo mejor!"
        in 11..20 -> "¡Nada mal! Sigue practicando."
        in 21..35 -> "¡Muy bien! Tienes buen ritmo."
        else      -> "¡Eres una máquina! 🔥"
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}