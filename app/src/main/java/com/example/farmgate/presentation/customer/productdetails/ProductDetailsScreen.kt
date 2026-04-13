package com.example.farmgate.presentation.customer.productdetails


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductDetailsScreen(
    uiState: ProductDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Product Details",
                    style = MaterialTheme.typography.headlineMedium
                )
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Product Details",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(onClick = onRetry) {
                    Text("Retry")
                }

                TextButton(onClick = onBackClick) {
                    Text("Back")
                }
            }
        }

        uiState.product != null -> {
            val product = uiState.product
            val unitLabel = product.unitType.name.lowercase()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = onBackClick
                ) {
                    Text("← Back")
                }

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Overview",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Category: ${product.category ?: "-"}")
                        Text(text = "Price: ${product.pricePerUnit} per $unitLabel")
                        Text(text = "Available: ${product.availableQuantity} $unitLabel")
                        Text(text = "Description: ${product.description ?: "-"}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pickup Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Area: ${product.pickupArea}")
                        Text(text = "City: ${product.cityName}")
                        Text(text = "Address: ${product.pickupAddress}")
                        Text(text = "Instructions: ${product.instructions ?: "-"}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Farmer",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Name: ${product.farmerName}")
                        Text(text = "Phone: ${product.farmerPhone ?: "-"}")
                    }
                }
            }
        }
    }
}