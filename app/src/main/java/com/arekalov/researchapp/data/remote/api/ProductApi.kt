package com.arekalov.researchapp.data.remote.api

import com.arekalov.researchapp.data.remote.dto.ProductDto
import com.arekalov.researchapp.data.remote.dto.ProductsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApi(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://dummyjson.com"
        private const val PRODUCTS_ENDPOINT = "$BASE_URL/products"
    }
    
    suspend fun getProducts(limit: Int, skip: Int): ProductsResponseDto {
        return client.get(PRODUCTS_ENDPOINT) {
            parameter("limit", limit)
            parameter("skip", skip)
            parameter("select", "id,title,description,price,rating,thumbnail,images")
        }.body()
    }
    
    suspend fun getProductById(id: Int): ProductDto {
        return client.get("$PRODUCTS_ENDPOINT/$id") {
            parameter("select", "id,title,description,price,rating,thumbnail,images")
        }.body()
    }
}
