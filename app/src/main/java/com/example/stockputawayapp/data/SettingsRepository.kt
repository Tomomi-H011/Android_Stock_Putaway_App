package com.example.stockputawayapp.data


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepository {

    private val _selectedDepartment = MutableStateFlow<Department?>(null)
    val selectedDepartment: StateFlow<Department?> = _selectedDepartment

    fun updateDepartment(department: Department) {
        _selectedDepartment.value = department
    }
}
