package com.arekalov.researchapp.presentation.products

import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.domain.model.Product

data class ProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val paginationMode: PaginationMode = PaginationMode.SEAMLESS
) {
    companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGES = 5
    }
}
