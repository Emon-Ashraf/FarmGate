package com.example.farmgate.presentation.customer.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.farmgate.presentation.components.ProductCard

@Composable
fun CustomerHomeScreen(
    uiState: CustomerHomeUiState,
    onCitySelected: (Long) -> Unit,
    onRetry: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Local pickup marketplace",
                style = MaterialTheme.typography.bodyMedium
            )
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.screenErrorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = uiState.screenErrorMessage,
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "FarmGate",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Local pickup marketplace",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Text(
                text = "Welcome, ${uiState.fullName}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        item {
            Text(
                text = "Choose city",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(uiState.cities) { city ->
            val selected = city.id == uiState.selectedCityId

            AssistChip(
                onClick = { onCitySelected(city.id) },
                label = {
                    Text(
                        text = if (selected) "${city.name} ✓" else city.name
                    )
                },
                colors = AssistChipDefaults.assistChipColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Selected city: ${uiState.selectedCityName ?: "-"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text(
                text = "Products",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (uiState.isProductsLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (uiState.productsErrorMessage != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = uiState.productsErrorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = "Retry")
                    }
                }
            }
        } else if (uiState.products.isEmpty()) {
            item {
                Text(
                    text = "No products found for this city.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(uiState.products) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) }
                )
            }
        }
    }
}