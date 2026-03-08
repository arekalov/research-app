package com.arekalov.researchapp.presentation.settings

import com.arekalov.researchapp.domain.model.PaginationMode

data class SettingsState(
    val selectedMode: PaginationMode = PaginationMode.SEAMLESS
)
