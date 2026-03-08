package com.arekalov.researchapp.data.remote.dto

import com.arekalov.researchapp.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        thumbnail = thumbnail,
        images = images
    )
}
