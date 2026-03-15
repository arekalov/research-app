package com.arekalov.researchapp.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arekalov.researchapp.BuildConfig
import com.arekalov.researchapp.domain.model.PaginationMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Режим",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            PaginationModeOption(
                title = "Режим 1",
                description = "",
                selected = state.selectedMode == PaginationMode.SEAMLESS,
                onClick = { viewModel.onModeSelected(PaginationMode.SEAMLESS) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            PaginationModeOption(
                title = "Режим 2",
                description = "",
                selected = state.selectedMode == PaginationMode.PROGRESS_BAR,
                onClick = { viewModel.onModeSelected(PaginationMode.PROGRESS_BAR) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            PaginationModeOption(
                title = "Режим 3",
                description = "",
                selected = state.selectedMode == PaginationMode.PAGED,
                onClick = { viewModel.onModeSelected(PaginationMode.PAGED) }
            )
            
            if (BuildConfig.FLAVOR == "dev") {
                Spacer(modifier = Modifier.height(8.dp))
                
                PaginationModeOption(
                    title = "DEV: Бесконечная загрузка",
                    description = "Прогресс-бар + бесконечная лента + подсветка первых 100",
                    selected = state.selectedMode == PaginationMode.DEV,
                    onClick = { viewModel.onModeSelected(PaginationMode.DEV) }
                )
            }
        }
    }
}

@Composable
private fun PaginationModeOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
