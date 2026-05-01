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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<GameType?>(null)
    val selectedFilter: StateFlow<GameType?> = _selectedFilter.asStateFlow()

    val sessions: StateFlow<List<GameSession>> = sessionStore.sessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredSessions: StateFlow<List<GameSession>> = combine(
        sessions, _selectedFilter
    ) { sessions, filter ->
        if (filter == null) sessions
        else sessions.filter { it.gameType == filter }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalSessions: StateFlow<Int> = filteredSessions
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val bestScore: StateFlow<Int> = filteredSessions
        .map { it.maxOfOrNull { s -> s.score } ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val averageScore: StateFlow<Int> = filteredSessions
        .map { if (it.isEmpty()) 0 else it.sumOf { s -> s.score } / it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setFilter(filter: GameType?) {
        _selectedFilter.value = filter
    }
}