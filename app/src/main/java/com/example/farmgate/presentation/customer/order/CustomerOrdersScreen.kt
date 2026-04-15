package com.example.farmgate.presentation.customer.order


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomerOrdersScreen(
    uiState: CustomerOrdersUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onOrderClick: (Long) -> Unit
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
                    text = "My Orders",
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
                    text = "My Orders",
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

        else -> {
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
                        text = "My Orders",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                if (uiState.orders.isEmpty()) {
                    item {
                        Text(
                            text = "No orders found.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(uiState.orders) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOrderClick(order.id) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Order #${order.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = "Status: ${order.status.name}")
                                Text(text = "Farmer: ${order.counterpartyName ?: "-"}")
                                Text(text = "Estimated Total: ${order.estimatedProductTotal}")
                                Text(text = "Service Fee: ${order.serviceFeeAmount}")
                                Text(text = "Pickup Due: ${order.pickupDueAt}")
                            }
                        }
                    }
                }
            }
        }
    }
}