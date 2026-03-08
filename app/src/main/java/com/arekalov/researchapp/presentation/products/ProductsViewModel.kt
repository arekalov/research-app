package com.arekalov.researchapp.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.domain.preferences.PreferencesManager
import com.arekalov.researchapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(ProductsState())
    val state = _state.asStateFlow()
    
    init {
        observePaginationMode()
        loadInitialPage()
    }
    
    private fun observePaginationMode() {
        viewModelScope.launch {
            preferencesManager.paginationMode.collect { mode ->
                val needsReset = _state.value.paginationMode != mode && _state.value.products.isNotEmpty()
                _state.update { it.copy(paginationMode = mode) }
                
                if (needsReset) {
                    resetAndLoadFirstPage()
                }
            }
        }
    }
    
    private fun loadInitialPage() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val isUnlimited = _state.value.paginationMode == PaginationMode.DEV
            
            repository.getProducts(page = 0, pageSize = ProductsState.PAGE_SIZE, unlimited = isUnlimited)
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            isLoading = false,
                            currentPage = 0,
                            hasMorePages = if (isUnlimited) {
                                products.size == ProductsState.PAGE_SIZE
                            } else {
                                products.size == ProductsState.PAGE_SIZE && 0 < ProductsState.MAX_PAGES - 1
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Неизвестная ошибка"
                        )
                    }
                }
        }
    }
    
    private fun resetAndLoadFirstPage() {
        _state.update {
            it.copy(
                products = emptyList(),
                currentPage = 0,
                hasMorePages = true,
                error = null
            )
        }
        loadInitialPage()
    }
    
    fun onIntent(intent: ProductsIntent) {
        when (intent) {
            is ProductsIntent.LoadNextPage -> loadNextPage()
            is ProductsIntent.LoadPreviousPage -> loadPreviousPage()
            is ProductsIntent.Retry -> loadInitialPage()
            is ProductsIntent.NavigateToDetails -> {
                // Навигация будет обработана в UI
            }
            is ProductsIntent.NavigateToSettings -> {
                // Навигация будет обработана в UI
            }
        }
    }
    
    private fun loadNextPage() {
        val currentState = _state.value
        
        if (currentState.isLoadingMore || !currentState.hasMorePages) return
        
        viewModelScope.launch {
            val nextPage = currentState.currentPage + 1
            
            when (currentState.paginationMode) {
                PaginationMode.PROGRESS_BAR, PaginationMode.DEV -> {
                    _state.update { it.copy(isLoadingMore = true) }
                    delay(2000) // Задержка в 2 секунды
                    loadPage(nextPage, isLoadingMore = true)
                }
                PaginationMode.SEAMLESS, PaginationMode.PAGED -> {
                    loadPage(nextPage, isLoadingMore = currentState.paginationMode != PaginationMode.PAGED)
                }
            }
        }
    }
    
    private fun loadPreviousPage() {
        val currentState = _state.value
        
        if (currentState.currentPage == 0 || currentState.isLoading) return
        
        viewModelScope.launch {
            val previousPage = currentState.currentPage - 1
            loadPage(previousPage, isLoadingMore = false, isPagedMode = true)
        }
    }
    
    private suspend fun loadPage(page: Int, isLoadingMore: Boolean, isPagedMode: Boolean = false) {
        if (!isLoadingMore && !isPagedMode) {
            _state.update { it.copy(isLoading = true) }
        }
        
        val isUnlimited = _state.value.paginationMode == PaginationMode.DEV
        
        repository.getProducts(page = page, pageSize = ProductsState.PAGE_SIZE, unlimited = isUnlimited)
            .onSuccess { newProducts ->
                _state.update { currentState ->
                    val updatedProducts = if (isPagedMode || currentState.paginationMode == PaginationMode.PAGED) {
                        newProducts
                    } else {
                        currentState.products + newProducts
                    }
                    
                    currentState.copy(
                        products = updatedProducts,
                        isLoading = false,
                        isLoadingMore = false,
                        currentPage = page,
                        hasMorePages = if (isUnlimited) {
                            newProducts.size == ProductsState.PAGE_SIZE
                        } else {
                            newProducts.size == ProductsState.PAGE_SIZE && page < ProductsState.MAX_PAGES - 1
                        },
                        error = null
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
            }
    }
}
