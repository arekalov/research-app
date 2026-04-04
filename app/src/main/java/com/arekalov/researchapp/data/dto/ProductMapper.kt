package com.arekalov.researchapp.data.dto

import com.arekalov.researchapp.domain.model.Product

fun ProductDto.toDomain(isInFirstHundred: Boolean = false, globalIndex: Int = -1): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
        images = images,
        isInFirstHundred = isInFirstHundred,
        globalIndex = globalIndex
    )
}
