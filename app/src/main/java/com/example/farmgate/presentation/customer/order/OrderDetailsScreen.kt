package com.example.farmgate.presentation.customer.order

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
fun OrderDetailsScreen(
    uiState: OrderDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onCancelNoteChanged: (String) -> Unit,
    onPaymentReferenceChanged: (String) -> Unit,
    onCancelOrderClick: () -> Unit,
    onConfirmFeeClick: () -> Unit,
    onRateFarmerClick: (Long) -> Unit,
    onReportIssueClick: (Long) -> Unit
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
                    text = "Order Details",
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
                    text = "Order Details",
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
                        Text(
                            text = "Status",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Current Status: ${order.status.name}")
                        Text(text = "Cancellation Reason: ${order.cancellationReason?.name ?: "-"}")
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
                            text = "People",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Customer: ${order.customerName ?: "-"}")
                        Text(text = "Farmer: ${order.farmerName ?: "-"}")
                        Text(text = "Farmer Phone: ${order.farmerPhone ?: "-"}")
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
                            text = "Amounts",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "Estimated Total: ${order.estimatedProductTotal}")
                        Text(text = "Actual Total: ${order.actualProductTotal ?: "-"}")
                        Text(text = "Service Fee: ${order.serviceFeeAmount}")
                        Text(text = "Fee Paid At: ${order.feePaidAt ?: "-"}")
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
                            text = "Pickup",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = "City: ${order.pickupCity ?: "-"}")
                        Text(text = "Area: ${order.pickupArea ?: "-"}")
                        Text(text = "Address: ${order.pickupAddress ?: "-"}")
                        Text(text = "Instructions: ${order.pickupInstructions ?: "-"}")
                        Text(text = "Pickup Due: ${order.pickupDueAt}")
                        Text(text = "Pickup Code: ${order.pickupCode ?: "-"}")
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
                            text = "Items",
                            style = MaterialTheme.typography.titleMedium
                        )

                        order.items.forEach { item ->
                            Text(
                                text = "${item.productName} - ${item.orderedQuantity} ${item.unitType.name.lowercase()} x ${item.unitPrice}"
                            )
                        }
                    }
                }

                order.payment?.let { payment ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Payment",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "Amount: ${payment.amount}")
                            Text(text = "Status: ${payment.status.name}")
                            Text(text = "Reference: ${payment.transactionReference ?: "-"}")
                            Text(text = "Paid At: ${payment.paidAt ?: "-"}")
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

                if (order.status == OrderStatus.Pending || order.status == OrderStatus.AwaitingFee) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Cancel Order",
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedTextField(
                                value = uiState.cancelNote,
                                onValueChange = onCancelNoteChanged,
                                label = { Text("Optional note") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = onCancelOrderClick,
                                enabled = !uiState.isCancelling,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (uiState.isCancelling) "Cancelling..." else "Cancel Order"
                                )
                            }
                        }
                    }
                }

                if (order.status == OrderStatus.AwaitingFee) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Confirm Service Fee",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("This confirms only the platform service fee.")
                            Text("Product payment is still made directly to farmer at pickup.")

                            OutlinedTextField(
                                value = uiState.paymentReference,
                                onValueChange = onPaymentReferenceChanged,
                                label = { Text("Optional payment reference") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                )
                            )

                            Button(
                                onClick = onConfirmFeeClick,
                                enabled = !uiState.isConfirmingFee,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (uiState.isConfirmingFee) "Confirming..." else "Confirm Service Fee"
                                )
                            }
                        }
                    }
                }

                if (order.status == OrderStatus.Completed) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "After Pickup",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("You can now rate the farmer or report an issue for this completed order.")

                            Button(
                                onClick = { onRateFarmerClick(order.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Rate Farmer")
                            }

                            Button(
                                onClick = { onReportIssueClick(order.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Report Issue")
                            }
                        }
                    }
                }
            }
        }
    }
}