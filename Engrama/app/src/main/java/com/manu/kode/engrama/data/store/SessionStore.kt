package com.manu.kode.engrama.data.store

import com.manu.kode.engrama.data.model.Division
import com.manu.kode.engrama.data.model.GameSession
import com.manu.kode.engrama.data.repository.SessionRepository
import com.manu.kode.engrama.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val settingsRepository: SettingsRepository,
    private val settingsStore: SettingsStore
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val sessions: StateFlow<List<GameSession>> = sessionRepository.sessions
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun addSession(session: GameSession) {
        scope.launch {
            sessionRepository.addSession(session)
            recalculateDivision()
        }
    }

    private fun recalculateDivision() {
        val totalPoints = sessions.value.sumOf { it.score }
        val earned = Division.fromPoints(totalPoints)
        val current = settingsStore.settings.value.profile.division

        if (earned != current) {
            val updated = settingsStore.settings.value.copy(
                profile = settingsStore.settings.value.profile.copy(division = earned)
            )
            settingsStore.save(updated)
        }
    }

    val totalPoints: Int get() = sessions.value.sumOf { it.score }

    val currentDivision: Division
        get() = Division.fromPoints(totalPoints)
}