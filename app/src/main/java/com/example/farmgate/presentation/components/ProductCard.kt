package com.example.farmgate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                    ProductImagePlaceholder(productName = product.name)
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(9.dp),
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                ) {
                    Text(
                        text = "Pickup",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary
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
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.farmerName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${product.pickupArea}, ${product.cityName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "BDT $formattedPrice / $unitLabel",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "$formattedAvailable $unitLabel",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductImagePlaceholder(
    productName: String
) {
    Surface(
        modifier = Modifier.size(54.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = productName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
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