package com.arekalov.researchapp.domain.repository

import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.domain.model.ProductListPage

interface ProductRepository {
    suspend fun getProducts(page: Int, pageSize: Int, unlimited: Boolean = false): Result<ProductListPage>
    suspend fun getProductById(id: Int): Result<Product>
}
