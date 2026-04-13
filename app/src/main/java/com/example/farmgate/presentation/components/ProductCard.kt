package com.example.farmgate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.ProductSummary

@Composable
fun ProductCard(
    product: ProductSummary,
    modifier: Modifier = Modifier
) {
    val unitLabel = product.unitType.name.lowercase()

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Farmer: ${product.farmerName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Pickup area: ${product.pickupArea}, ${product.cityName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Price: ${product.pricePerUnit} per $unitLabel",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Available: ${product.availableQuantity} $unitLabel",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}