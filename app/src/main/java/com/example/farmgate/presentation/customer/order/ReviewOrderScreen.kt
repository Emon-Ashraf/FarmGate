package com.example.farmgate.presentation.customer.order


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewOrderScreen(
    uiState: ReviewOrderUiState,
    onBackClick: () -> Unit,
    onQuantityChanged: (Long, String) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onClearDraft: () -> Unit,
    onSubmitOrder: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    val draft = uiState.draft

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TextButton(onClick = onBackClick) {
                Text("← Back")
            }
        }

        item {
            Text(
                text = "Review Order",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (draft == null) {
            item {
                Text("No items in your order draft.")
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pickup Summary",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Farmer: ${draft.farmerName}")
                        Text(text = "Area: ${draft.pickupArea}")
                        Text(text = "City: ${draft.cityName}")
                        Text(text = "Address: ${draft.pickupAddress}")
                    }
                }
            }

            items(draft.items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = item.productName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Price: ${item.pricePerUnit} per ${item.unitType.name.lowercase()}")
                        Text(text = "Available: ${item.availableQuantity}")

                        OutlinedTextField(
                            value = item.selectedQuantity.toString(),
                            onValueChange = { onQuantityChanged(item.productId, it) },
                            label = { Text("Quantity") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        TextButton(onClick = { onRemoveItem(item.productId) }) {
                            Text("Remove")
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Summary",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Estimated Product Total: ${draft.estimatedProductTotal}")
                        Text(text = "Service fee is paid in-app.")
                        Text(text = "Product payment is made directly to farmer at pickup.")
                    }
                }
            }

            uiState.errorMessage?.let { message ->
                item {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onClearDraft,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSubmitting
                    ) {
                        Text("Clear Draft")
                    }

                    Button(
                        onClick = onSubmitOrder,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSubmitting
                    ) {
                        Text(if (uiState.isSubmitting) "Submitting..." else "Place Order")
                    }
                }
            }
        }
    }
}