package com.arekalov.researchapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.domain.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()
    
    init {
        loadCurrentMode()
    }
    
    private fun loadCurrentMode() {
        viewModelScope.launch {
            preferencesManager.paginationMode.collect { mode ->
                _state.update { it.copy(selectedMode = mode) }
            }
        }
    }
    
    fun onModeSelected(mode: PaginationMode) {
        viewModelScope.launch {
            preferencesManager.setPaginationMode(mode)
        }
    }
}
