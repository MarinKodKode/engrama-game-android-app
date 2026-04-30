package com.manu.kode.engrama.features.math.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.kode.engrama.data.model.AppSettings
import com.manu.kode.engrama.data.store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChooseOperationViewModel @Inject constructor(
    private val settingsStore: SettingsStore
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsStore.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )
}