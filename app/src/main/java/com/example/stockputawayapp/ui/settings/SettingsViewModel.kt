package com.example.stockputawayapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockputawayapp.data.Department
import com.example.stockputawayapp.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Combine repository flow into UI state
    val uiState: StateFlow<SettingsUiState> =
        settingsRepository.selectedDepartment
            .map { department -> SettingsUiState(selectedDepartment = department) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SettingsUiState()
            )

    fun selectDepartment(department: Department) {
        settingsRepository.updateDepartment(department)
    }
}



data class SettingsUiState(
    val selectedDepartment: Department? = null
)
