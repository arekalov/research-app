package com.arekalov.researchapp.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Спокойная тёмная тема: приглушённые акценты, мягкий фон, без «неонового» динамического инверта. */
private val RefinedDarkColorScheme = darkColorScheme(
    primary = Color(0xFF8FA8E8),
    onPrimary = Color(0xFF0F1419),
    primaryContainer = Color(0xFF2D3548),
    onPrimaryContainer = Color(0xFF8B96AB),
    secondary = Color(0xFFB4B9C9),
    onSecondary = Color(0xFF252831),
    secondaryContainer = Color(0xFF353945),
    onSecondaryContainer = Color(0xFFE1E3EE),
    tertiary = Color(0xFFD4B8C8),
    onTertiary = Color(0xFF2A2228),
    tertiaryContainer = Color(0xFF453B44),
    onTertiaryContainer = Color(0xFFEEDAE8),
    background = Color(0xFF121318),
    onBackground = Color(0xFFE2E2E8),
    surface = Color(0xFF181A20),
    onSurface = Color(0xFFE2E2E8),
    surfaceVariant = Color(0xFF3D424C),
    onSurfaceVariant = Color(0xFFC2C6D0),
    outline = Color(0xFF8C919C),
    outlineVariant = Color(0xFF464A54),
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF561E19),
    errorContainer = Color(0xFF73342D),
    onErrorContainer = Color(0xFFFFDAD4)
)

/**
 * Светлая тема: те же роли, что в [RefinedDarkColorScheme], с «высоким ключом» —
 * спокойные контейнеры без кислотных дефолтов M3.
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE8EBF4),
    onPrimaryContainer = Color(0xFF303848),
    secondary = PurpleGrey40,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEFEFF2),
    onSecondaryContainer = Color(0xFF393B44),
    tertiary = Pink40,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF5EEF2),
    onTertiaryContainer = Color(0xFF48404A),
    background = Color(0xFFF8F9FB),
    onBackground = Color(0xFF1B1D22),
    surface = Color(0xFFFDFDFE),
    onSurface = Color(0xFF1B1D22),
    surfaceVariant = Color(0xFFE2E4EA),
    onSurfaceVariant = Color(0xFF5C5F68),
    outline = Color(0xFF8A8E98),
    outlineVariant = Color(0xFFC8CCD4)
)

@Composable
fun ResearchappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> RefinedDarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview(showBackground = true, name = "Тема — светлая")
@Composable
private fun ResearchappThemePreviewLight() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Research App",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Основной текст на surface",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Вторичный onSurfaceVariant",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Тема — тёмная", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ResearchappThemePreviewDark() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Research App",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Основной текст",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}