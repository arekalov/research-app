package com.arekalov.researchapp.domain.model

enum class PaginationMode {
    SEAMLESS,        // Бесшовная пагинация
    PROGRESS_BAR,    // С прогресс-баром
    PAGED,           // Страничная пагинация
    DEV              // Dev режим: бесконечный с прогресс-баром и подсветкой первых 100
}
