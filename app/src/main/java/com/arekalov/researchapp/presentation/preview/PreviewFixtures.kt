package com.arekalov.researchapp.presentation.preview

import com.arekalov.researchapp.domain.model.PaginationMode
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.presentation.products.ProductsState

private const val PLACEHOLDER_IMAGE = "https://picsum.photos/seed/preview/400/400"

fun previewProduct(
    id: Int = 1,
    title: String = "Пример товара с длинным названием",
    globalIndex: Int = 0,
    isInFirstHundred: Boolean = true
): Product = Product(
    id = id,
    title = title,
    description = "Краткое описание товара для превью в Android Studio. Текст может занимать несколько строк.",
    price = 12_345.0,
    rating = 4.7,
    thumbnail = PLACEHOLDER_IMAGE,
    images = listOf(PLACEHOLDER_IMAGE),
    isInFirstHundred = isInFirstHundred,
    globalIndex = globalIndex
)

fun previewProducts(count: Int = 4): List<Product> = List(count) { i ->
    previewProduct(
        id = i + 1,
        title = "Товар ${i + 1}",
        globalIndex = i,
        isInFirstHundred = i < 100
    )
}

fun previewProductsState(
    mode: PaginationMode = PaginationMode.SEAMLESS,
    products: List<Product> = previewProducts(),
    isLoading: Boolean = false,
    isLoadingMore: Boolean = false,
    error: String? = null,
    currentPage: Int = 0,
    hasMorePages: Boolean = true
): ProductsState = ProductsState(
    products = products,
    isLoading = isLoading,
    isLoadingMore = isLoadingMore,
    error = error,
    currentPage = currentPage,
    hasMorePages = hasMorePages,
    paginationMode = mode
)
