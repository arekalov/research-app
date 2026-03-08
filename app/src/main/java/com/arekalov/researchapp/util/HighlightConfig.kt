package com.arekalov.researchapp.util

import com.arekalov.researchapp.BuildConfig

object HighlightConfig {
    
    private val highlightedIndices: Set<Int> by lazy {
        val config = BuildConfig.HIGHLIGHTED_CARDS
        if (config.isBlank()) {
            emptySet()
        } else {
            config.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .toSet()
        }
    }
    
    fun isHighlighted(index: Int): Boolean {
        return highlightedIndices.contains(index)
    }
    
    fun hasHighlightedCards(): Boolean {
        return highlightedIndices.isNotEmpty()
    }
}
