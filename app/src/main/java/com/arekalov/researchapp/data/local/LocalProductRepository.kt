package com.arekalov.researchapp.data.local

import android.content.Context
import com.arekalov.researchapp.data.dto.ProductDto
import com.arekalov.researchapp.data.dto.toDomain
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.domain.model.ProductListPage
import com.arekalov.researchapp.domain.repository.ProductRepository
import com.arekalov.researchapp.presentation.products.ProductsState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

    override suspend fun getProducts(page: Int, pageSize: Int, unlimited: Boolean): Result<ProductListPage> {
        return try {
            val skip = page * pageSize

            val slice = if (unlimited) {
                if (skip >= allProducts.size) {
                    emptyList()
                } else {
                    allProducts.drop(skip).take(pageSize)
                }
            } else {
                if (skip >= WORKING_MODES_MAX_ITEMS) {
                    emptyList()
                } else {
                    allProducts.drop(skip).take(pageSize.coerceAtMost(WORKING_MODES_MAX_ITEMS - skip))
                }
            }

            val domainProducts = slice.mapIndexed { index, productDto ->
                val globalIndex = skip + index
                productDto.toDomain(
                    isInFirstHundred = globalIndex < FIRST_HUNDRED_COUNT,
                    globalIndex = globalIndex
                )
            }

            val hasMore = if (unlimited) {
                skip + slice.size < allProducts.size
            } else {
                slice.size == pageSize && page < ProductsState.MAX_PAGES - 1
            }

            Result.success(ProductListPage(products = domainProducts, hasMore = hasMore))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val index = allProducts.indexOfFirst { it.id == id }
            val productDto = allProducts.getOrNull(index)
            if (productDto != null) {
                Result.success(
                    productDto.toDomain(
                        isInFirstHundred = index < FIRST_HUNDRED_COUNT,
                        globalIndex = index
                    )
                )
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        /** В обычных режимах пагинации не больше стольких товаров; весь каталог (200) — только в режиме DEV. */
        const val WORKING_MODES_MAX_ITEMS = 100

        private const val FIRST_HUNDRED_COUNT = 100
    }
}

