package com.manu.kode.engrama.data.store

import com.manu.kode.engrama.data.model.AppSettings
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
class SettingsStore @Inject constructor(
    private val repository: SettingsRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = AppSettings()
        )

    fun save(settings: AppSettings) {
        scope.launch {
            repository.save(settings)
        }
    }

    fun updateName(name: String) {
        save(settings.value.copy(profile = settings.value.profile.copy(name = name)))
    }

    fun updateHaptic(enabled: Boolean) {
        save(settings.value.copy(hapticOnError = enabled))
    }
}