package com.example.farmgate.presentation.admin.product


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminProductModerationScreen(
    uiState: AdminProductModerationUiState,
    onDeactivateProduct: (Long) -> Unit,
    onActivateProduct: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Admin Product Moderation",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        uiState.errorMessage?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        uiState.successMessage?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(uiState.targets) { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = product.productName, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Product ID: ${product.productId}")
                    Text(text = "Farmer: ${product.farmerName}")
                    Text(text = "Active: ${product.isActive}")

                    if (product.isActive) {
                        Button(
                            onClick = { onDeactivateProduct(product.productId) },
                            enabled = !uiState.isSubmitting,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Deactivate Product")
                        }
                    } else {
                        Button(
                            onClick = { onActivateProduct(product.productId) },
                            enabled = !uiState.isSubmitting,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Activate Product")
                        }
                    }
                }
            }
        }
    }
}