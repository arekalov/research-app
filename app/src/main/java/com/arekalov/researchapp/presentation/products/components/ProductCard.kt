package com.arekalov.researchapp.presentation.products.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arekalov.researchapp.domain.model.Product
import com.arekalov.researchapp.presentation.preview.previewProduct
import com.arekalov.researchapp.ui.theme.ResearchappTheme
import com.arekalov.researchapp.util.HighlightConfig
import com.arekalov.researchapp.util.formatRubPrice
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showFirstHundredHighlight: Boolean = false
) {
    // Проверяем подсветку из BuildConfig
    val isHighlightedByConfig = HighlightConfig.isHighlighted(product.globalIndex)
    
    // Определяем цвет фона
    val backgroundColor = when {
        isHighlightedByConfig -> Color(0xFFE1F5FE) // Светло-голубой для BuildConfig карточек
        showFirstHundredHighlight && product.isInFirstHundred -> Color(0xFFFFF8E1) // Светло-желтый для первых 100
        else -> MaterialTheme.colorScheme.surface
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatRubPrice(product.price),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (isHighlightedByConfig) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "#${product.globalIndex}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", product.rating),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Карточка — светлая")
@Composable
private fun ProductCardPreviewLight() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        ProductCard(
            product = previewProduct(title = "Тушь для ресниц с очень длинным названием"),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Карточка — тёмная", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductCardPreviewDark() {
    ResearchappTheme(darkTheme = true, dynamicColor = false) {
        ProductCard(
            product = previewProduct(title = "Пример товара"),
            onClick = {}
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, name = "Карточка — подсветка первых 100")
@Composable
private fun ProductCardPreviewFirstHundred() {
    ResearchappTheme {
        ProductCard(
            product = previewProduct(globalIndex = 5, isInFirstHundred = true),
            onClick = {},
            showFirstHundredHighlight = true
        )
    }
}
