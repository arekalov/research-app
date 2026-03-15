package com.arekalov.researchapp.data.local

import android.content.Context
import com.arekalov.researchapp.data.remote.dto.ProductDto
import com.arekalov.researchapp.data.remote.dto.toDomain
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.domain.repository.ProductRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Serializable
data class ProductsResponse(
    val products: List<ProductDto>
)

class LocalProductRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : ProductRepository {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    private val allProducts: List<ProductDto> by lazy {
        val jsonString = context.assets.open("products.json").bufferedReader().use { it.readText() }
        val response = json.decodeFromString<ProductsResponse>(jsonString)
        response.products
    }
    
    override suspend fun getProducts(page: Int, pageSize: Int, unlimited: Boolean): Result<List<Product>> {
        return try {
            val skip = page * pageSize
            
            val products = if (unlimited) {
                if (skip >= allProducts.size) {
                    emptyList()
                } else {
                    allProducts.drop(skip).take(pageSize)
                }
            } else {
                val maxItems = 100
                if (skip >= maxItems) {
                    emptyList()
                } else {
                    allProducts.drop(skip).take(pageSize.coerceAtMost(maxItems - skip))
                }
            }
            
            val domainProducts = products.mapIndexed { index, productDto ->
                val globalIndex = skip + index
                productDto.toDomain(
                    isInFirstHundred = globalIndex < 100,
                    globalIndex = globalIndex
                )
            }
            
            Result.success(domainProducts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val productDto = allProducts.find { it.id == id }
            if (productDto != null) {
                Result.success(productDto.toDomain(isInFirstHundred = false, globalIndex = -1))
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
