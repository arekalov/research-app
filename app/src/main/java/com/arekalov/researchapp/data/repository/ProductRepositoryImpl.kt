package com.arekalov.researchapp.data.repository

import com.arekalov.researchapp.data.remote.api.ProductApi
import com.arekalov.researchapp.data.remote.dto.toDomain
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {
    
    companion object {
        private const val MAX_PRODUCTS = 100
    }
    
    override suspend fun getProducts(page: Int, pageSize: Int): Result<List<Product>> {
        return try {
            val skip = page * pageSize
            
            if (skip >= MAX_PRODUCTS) {
                return Result.success(emptyList())
            }
            
            val actualLimit = minOf(pageSize, MAX_PRODUCTS - skip)
            
            val response = api.getProducts(limit = actualLimit, skip = skip)
            val products = response.products.map { it.toDomain() }
            
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val product = api.getProductById(id).toDomain()
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
