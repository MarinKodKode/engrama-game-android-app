package com.manu.kode.engrama.features.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.data.model.GameSession
import com.manu.kode.engrama.data.model.GameType
import com.manu.kode.engrama.data.store.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<GameType?>(null)
    val selectedFilter: StateFlow<GameType?> = _selectedFilter.asStateFlow()

    val sessions: StateFlow<List<GameSession>> = sessionStore.sessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredSessions: List<GameSession> get() {
        val filter = _selectedFilter.value ?: return sessions.value
        return sessions.value.filter { it.gameType == filter }
    }

    val totalSessions: Int get() = filteredSessions.size
    val bestScore: Int get() = filteredSessions.maxOfOrNull { it.score } ?: 0
    val averageScore: Int get() = if (filteredSessions.isEmpty()) 0
    else filteredSessions.sumOf { it.score } / filteredSessions.size

    fun setFilter(filter: GameType?) {
        _selectedFilter.value = filter
    }
}