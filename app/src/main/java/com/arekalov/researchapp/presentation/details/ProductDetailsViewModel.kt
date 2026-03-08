package com.arekalov.researchapp.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.researchapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()
    
    init {
        val productId = savedStateHandle.get<String>("productId")?.toIntOrNull()
        if (productId != null) {
            loadProduct(productId)
        }
    }
    
    private fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            repository.getProductById(productId)
                .onSuccess { product ->
                    _state.update {
                        it.copy(
                            product = product,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Ошибка загрузки"
                        )
                    }
                }
        }
    }
    
    fun retry() {
        _state.value.product?.let { loadProduct(it.id) }
    }
}
