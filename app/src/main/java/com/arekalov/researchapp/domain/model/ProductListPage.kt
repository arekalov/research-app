package com.arekalov.researchapp.domain.model

data class ProductListPage(
    val products: List<Product>,
    val hasMore: Boolean
)
