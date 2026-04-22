package com.example.farmgate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.farmgate.data.model.ProductSummary
import com.example.farmgate.data.model.UnitType
import java.util.Locale

@Composable
fun ProductCard(
    product: ProductSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unitLabel = product.unitType.toDisplayLabel()
    val formattedPrice = formatNumber(product.pricePerUnit)
    val formattedAvailable = formatNumber(product.availableQuantity)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(Color(0xFFF2F4F3)),
                contentAlignment = Alignment.Center
            ) {
                if (!product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "No Image",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8A8A8A)
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.92f)
                ) {
                    Text(
                        text = "Pickup",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4D5B53)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.farmerName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6F7B74),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${product.pickupArea}, ${product.cityName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6F7B74),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer8()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BDT $formattedPrice / $unitLabel",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1A1A)
                    )

                    Text(
                        text = "$formattedAvailable $unitLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8A8A8A),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun Spacer8() {
    Box(modifier = Modifier.size(2.dp))
}

private fun UnitType.toDisplayLabel(): String {
    return when (this) {
        UnitType.Piece -> "piece"
        UnitType.Kg -> "kg"
        UnitType.Liter -> "liter"
        UnitType.Dozen -> "dozen"
        UnitType.Bundle -> "bundle"
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}