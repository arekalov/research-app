package com.arekalov.researchapp.presentation.products.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.presentation.preview.previewProducts
import com.arekalov.researchapp.presentation.products.ProductsState
import com.arekalov.researchapp.ui.theme.ResearchappTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi

@Composable
fun PagedPaginationList(
    products: List<Product>,
    currentPage: Int,
    totalPages: Int,
    hasNextPage: Boolean,
    hasPreviousPage: Boolean,
    onProductClick: (Int) -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    LaunchedEffect(currentPage) {
        listState.animateScrollToItem(0)
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = products,
                key = { it.id }
            ) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) }
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPreviousPage,
                    enabled = hasPreviousPage
                ) {
                    Text("Назад")
                }
                
                Text(
                    text = "Страница ${currentPage + 1} из $totalPages",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Button(
                    onClick = onNextPage,
                    enabled = hasNextPage
                ) {
                    Text("Вперёд")
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Страничная пагинация")
@Composable
private fun PagedPaginationListPreview() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        PagedPaginationList(
            products = previewProducts(4),
            currentPage = 1,
            totalPages = ProductsState.MAX_PAGES,
            hasNextPage = true,
            hasPreviousPage = true,
            onProductClick = {},
            onNextPage = {},
            onPreviousPage = {}
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Страничная — тёмная", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PagedPaginationListPreviewDark() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        PagedPaginationList(
            products = previewProducts(2),
            currentPage = 0,
            totalPages = ProductsState.MAX_PAGES,
            hasNextPage = true,
            hasPreviousPage = false,
            onProductClick = {},
            onNextPage = {},
            onPreviousPage = {}
        )
    }
}
