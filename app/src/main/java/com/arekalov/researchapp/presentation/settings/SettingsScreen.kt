package com.arekalov.researchapp.presentation.settings

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arekalov.researchapp.BuildConfig
import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.ui.theme.ResearchappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreenContent(
        selectedMode = state.selectedMode,
        onModeSelected = viewModel::onModeSelected,
        onNavigateBack = onNavigateBack,
        showDevOption = BuildConfig.FLAVOR == "dev"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreenContent(
    selectedMode: PaginationMode,
    onModeSelected: (PaginationMode) -> Unit,
    onNavigateBack: () -> Unit,
    showDevOption: Boolean
) {
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
                selected = selectedMode == PaginationMode.SEAMLESS,
                onClick = { onModeSelected(PaginationMode.SEAMLESS) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaginationModeOption(
                title = "Режим 2",
                description = "",
                selected = selectedMode == PaginationMode.PROGRESS_BAR,
                onClick = { onModeSelected(PaginationMode.PROGRESS_BAR) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaginationModeOption(
                title = "Режим 3",
                description = "",
                selected = selectedMode == PaginationMode.PAGED,
                onClick = { onModeSelected(PaginationMode.PAGED) }
            )

            if (showDevOption) {
                Spacer(modifier = Modifier.height(8.dp))

                PaginationModeOption(
                    title = "DEV: Бесконечная загрузка",
                    description = "Прогресс-бар + бесконечная лента + подсветка первых 100",
                    selected = selectedMode == PaginationMode.DEV,
                    onClick = { onModeSelected(PaginationMode.DEV) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Настройки — светлая")
@Composable
private fun SettingsPreviewLight() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        SettingsScreenContent(
            selectedMode = PaginationMode.SEAMLESS,
            onModeSelected = {},
            onNavigateBack = {},
            showDevOption = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Настройки — тёмная", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsPreviewDark() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        SettingsScreenContent(
            selectedMode = PaginationMode.DEV,
            onModeSelected = {},
            onNavigateBack = {},
            showDevOption = true
        )
    }
}
