package com.arekalov.researchapp.domain.repository

import com.arekalov.researchapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(page: Int, pageSize: Int): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
}
