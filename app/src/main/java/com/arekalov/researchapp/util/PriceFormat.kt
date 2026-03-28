package com.arekalov.researchapp.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arekalov.researchapp.ui.theme.ResearchappTheme
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

fun formatRubPrice(price: Double): String {
    val amount = price.roundToInt()
    val s = abs(amount).toString()
    val parts = mutableListOf<String>()
    var idx = s.length
    while (idx > 0) {
        val start = max(0, idx - 3)
        parts.add(0, s.substring(start, idx))
        idx = start
    }
    val sign = if (amount < 0) "-" else ""
    return "$sign${parts.joinToString(" ")} ₽"
}

@Preview(showBackground = true, name = "Формат цены ₽")
@Composable
private fun FormatRubPricePreview() {
    ResearchappTheme(darkTheme = false, dynamicColor = false) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = formatRubPrice(799.0),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = formatRubPrice(1_234_567.0),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
