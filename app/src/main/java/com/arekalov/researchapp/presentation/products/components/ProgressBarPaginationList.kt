package com.arekalov.researchapp.presentation.products.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.presentation.preview.previewProducts
import com.arekalov.researchapp.ui.theme.ResearchappTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@Composable
fun ProgressBarPaginationList(
    products: List<Product>,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    onProductClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    showFirstHundredHighlight: Boolean = false
) {
    val listState = rememberLazyListState()
    
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            
            lastVisibleItem != null &&
                    lastVisibleItem.index >= totalItems - 1 &&
                    !isLoadingMore &&
                    hasMorePages
        }
    }
    
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }
    
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.id) },
                showFirstHundredHighlight = showFirstHundredHighlight
            )
        }
        
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Progress bar список")
@Composable
private fun ProgressBarPaginationListPreview() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        ProgressBarPaginationList(
            products = previewProducts(4),
            isLoadingMore = true,
            hasMorePages = true,
            onProductClick = {},
            onLoadMore = {}
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Progress bar — DEV подсветка", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProgressBarPaginationListPreviewDev() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        ProgressBarPaginationList(
            products = previewProducts(3),
            isLoadingMore = false,
            hasMorePages = true,
            onProductClick = {},
            onLoadMore = {},
            showFirstHundredHighlight = true
        )
    }
}
