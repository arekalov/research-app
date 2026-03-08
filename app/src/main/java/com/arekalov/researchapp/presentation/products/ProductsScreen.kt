package com.arekalov.researchapp.presentation.products

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.presentation.products.components.PagedPaginationList
import com.arekalov.researchapp.presentation.products.components.ProgressBarPaginationList
import com.arekalov.researchapp.presentation.products.components.SeamlessPaginationList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
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
                                onLoadMore = { viewModel.onIntent(ProductsIntent.LoadNextPage) }
                            )
                        }
                        
                        PaginationMode.PROGRESS_BAR -> {
                            ProgressBarPaginationList(
                                products = state.products,
                                isLoadingMore = state.isLoadingMore,
                                hasMorePages = state.hasMorePages,
                                onProductClick = onNavigateToDetails,
                                onLoadMore = { viewModel.onIntent(ProductsIntent.LoadNextPage) }
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
                                onNextPage = { viewModel.onIntent(ProductsIntent.LoadNextPage) },
                                onPreviousPage = { viewModel.onIntent(ProductsIntent.LoadPreviousPage) }
                            )
                        }
                        
                        PaginationMode.DEV -> {
                            ProgressBarPaginationList(
                                products = state.products,
                                isLoadingMore = state.isLoadingMore,
                                hasMorePages = state.hasMorePages,
                                onProductClick = onNavigateToDetails,
                                onLoadMore = { viewModel.onIntent(ProductsIntent.LoadNextPage) },
                                showFirstHundredHighlight = true
                            )
                        }
                    }
                }
            }
        }
    }
}
