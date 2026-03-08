package com.arekalov.researchapp.presentation.details

import com.arekalov.researchapp.domain.model.Product

data class ProductDetailsState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
