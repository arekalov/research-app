package com.arekalov.researchapp.presentation.products

sealed interface ProductsIntent {
    data object LoadNextPage : ProductsIntent
    data object LoadPreviousPage : ProductsIntent
    data object Retry : ProductsIntent
    data class NavigateToDetails(val productId: Int) : ProductsIntent
    data object NavigateToSettings : ProductsIntent
}
