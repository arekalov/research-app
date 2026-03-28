package com.arekalov.researchapp.presentation.products

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.presentation.preview.previewProductsState
import com.arekalov.researchapp.presentation.products.components.PagedPaginationList
import com.arekalov.researchapp.presentation.products.components.ProgressBarPaginationList
import com.arekalov.researchapp.presentation.products.components.SeamlessPaginationList
import com.arekalov.researchapp.ui.theme.ResearchappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProductsScreenContent(
        state = state,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToSettings = onNavigateToSettings,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsScreenContent(
    state: ProductsState,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    onIntent: (ProductsIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Research App",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Настройки"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading && state.products.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null && state.products.isEmpty() -> {
                    Text(
                        text = state.error ?: "Ошибка",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    when (state.paginationMode) {
                        PaginationMode.SEAMLESS -> {
                            SeamlessPaginationList(
                                products = state.products,
                                isLoadingMore = state.isLoadingMore,
                                hasMorePages = state.hasMorePages,
                                onProductClick = onNavigateToDetails,
                                onLoadMore = { onIntent(ProductsIntent.LoadNextPage) }
                            )
                        }

                        PaginationMode.PROGRESS_BAR -> {
                            ProgressBarPaginationList(
                                products = state.products,
                                isLoadingMore = state.isLoadingMore,
                                hasMorePages = state.hasMorePages,
                                onProductClick = onNavigateToDetails,
                                onLoadMore = { onIntent(ProductsIntent.LoadNextPage) }
                            )
                        }

                        PaginationMode.PAGED -> {
                            PagedPaginationList(
                                products = state.products,
                                currentPage = state.currentPage,
                                totalPages = ProductsState.MAX_PAGES,
                                hasNextPage = state.hasMorePages,
                                hasPreviousPage = state.currentPage > 0,
                                onProductClick = onNavigateToDetails,
                                onNextPage = { onIntent(ProductsIntent.LoadNextPage) },
                                onPreviousPage = { onIntent(ProductsIntent.LoadPreviousPage) }
                            )
                        }

                        PaginationMode.DEV -> {
                            ProgressBarPaginationList(
                                products = state.products,
                                isLoadingMore = state.isLoadingMore,
                                hasMorePages = state.hasMorePages,
                                onProductClick = onNavigateToDetails,
                                onLoadMore = { onIntent(ProductsIntent.LoadNextPage) },
                                showFirstHundredHighlight = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Список — светлая")
@Composable
private fun ProductsScreenContentPreviewLight() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        ProductsScreenContent(
            state = previewProductsState(),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Список — тёмная", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductsScreenContentPreviewDark() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        ProductsScreenContent(
            state = previewProductsState(),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Загрузка")
@Composable
private fun ProductsScreenContentPreviewLoading() {
    ResearchappTheme {
        ProductsScreenContent(
            state = previewProductsState(products = emptyList(), isLoading = true),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Ошибка")
@Composable
private fun ProductsScreenContentPreviewError() {
    ResearchappTheme {
        ProductsScreenContent(
            state = previewProductsState(products = emptyList(), error = "Нет сети"),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Режим 2 — progress bar")
@Composable
private fun ProductsScreenContentPreviewProgressBar() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        ProductsScreenContent(
            state = previewProductsState(mode = PaginationMode.PROGRESS_BAR, isLoadingMore = true),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Режим 3 — страницы")
@Composable
private fun ProductsScreenContentPreviewPaged() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        ProductsScreenContent(
            state = previewProductsState(
                mode = PaginationMode.PAGED,
                currentPage = 2,
                hasMorePages = true
            ),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onIntent = {}
        )
    }
}
