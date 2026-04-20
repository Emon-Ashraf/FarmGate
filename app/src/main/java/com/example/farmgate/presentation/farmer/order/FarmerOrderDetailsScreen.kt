package com.example.farmgate.presentation.farmer.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.OrderStatus

@Composable
fun FarmerOrderDetailsScreen(
    uiState: FarmerOrderDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    onPickupCodeChanged: (String) -> Unit,
    onFulfilledQuantityChanged: (Long, String) -> Unit,
    onCompleteClick: () -> Unit
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
                    text = "Farmer Order Details",
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
                    text = "Farmer Order Details",
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

        uiState.order != null -> {
            val order = uiState.order

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onBackClick) {
                    Text("← Back")
                }

                Text(
                    text = "Order #${order.id}",
                    style = MaterialTheme.typography.headlineMedium
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Status", style = MaterialTheme.typography.titleMedium)
                        Text("Current Status: ${order.status.name}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("People", style = MaterialTheme.typography.titleMedium)
                        Text("Customer: ${order.customerName ?: "-"}")
                        Text("Farmer: ${order.farmerName ?: "-"}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Amounts", style = MaterialTheme.typography.titleMedium)
                        Text("Estimated Total: ${order.estimatedProductTotal}")
                        Text("Actual Total: ${order.actualProductTotal ?: "-"}")
                        Text("Service Fee: ${order.serviceFeeAmount}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Pickup", style = MaterialTheme.typography.titleMedium)
                        Text("City: ${order.pickupCity ?: "-"}")
                        Text("Area: ${order.pickupArea ?: "-"}")
                        Text("Address: ${order.pickupAddress ?: "-"}")
                        Text("Instructions: ${order.pickupInstructions ?: "-"}")
                        Text("Pickup Due: ${order.pickupDueAt}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Items", style = MaterialTheme.typography.titleMedium)

                        order.items.forEach { item ->
                            Text("${item.productName} - ordered: ${item.orderedQuantity} ${item.unitType.name.lowercase()} x ${item.unitPrice}")
                        }
                    }
                }

                if (uiState.actionErrorMessage != null) {
                    Text(
                        text = uiState.actionErrorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (uiState.actionSuccessMessage != null) {
                    Text(
                        text = uiState.actionSuccessMessage,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (order.status == OrderStatus.Pending) {
                    Button(
                        onClick = onAcceptClick,
                        enabled = !uiState.isAccepting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isAccepting) "Accepting..." else "Accept Order")
                    }

                    Button(
                        onClick = onRejectClick,
                        enabled = !uiState.isRejecting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isRejecting) "Rejecting..." else "Reject Order")
                    }
                }

                if (order.status == OrderStatus.Confirmed) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Complete Order by OTP",
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedTextField(
                                value = uiState.pickupCode,
                                onValueChange = onPickupCodeChanged,
                                label = { Text("Pickup Code") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                )
                            )

                            order.items.forEach { item ->
                                OutlinedTextField(
                                    value = uiState.fulfilledQuantities[item.id].orEmpty(),
                                    onValueChange = { onFulfilledQuantityChanged(item.id, it) },
                                    label = { Text("Fulfilled Quantity - ${item.productName}") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    )
                                )
                            }

                            Button(
                                onClick = onCompleteClick,
                                enabled = !uiState.isCompleting,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (uiState.isCompleting) "Completing..." else "Complete Order"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}