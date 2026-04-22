package com.example.farmgate.presentation.customer.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderStatus
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import java.util.Locale

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Order Details",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
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
            val statusUi = order.status.toUi()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Back"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            text = "Order #${order.id}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatusChip(
                                text = statusUi.label,
                                background = statusUi.background,
                                content = statusUi.content
                            )

                            Text(
                                text = statusUi.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = statusUi.description(order),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (order.status == OrderStatus.AwaitingFee) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0x1AF97316)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Commission payment pending",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFFB45309)
                                )

                                Text(
                                    text = "Platform fee must be paid to continue this order. Product payment is still made directly to the farmer at pickup.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFB45309)
                                )

                                OutlinedTextField(
                                    value = uiState.paymentReference,
                                    onValueChange = onPaymentReferenceChanged,
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    label = { Text("Optional payment reference") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF18D66B),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                FarmGatePrimaryButton(
                                    text = if (uiState.isConfirmingFee) {
                                        "Confirming..."
                                    } else {
                                        "Confirm Service Fee • BDT ${formatMoney(order.serviceFeeAmount)}"
                                    },
                                    onClick = onConfirmFeeClick,
                                    enabled = !uiState.isConfirmingFee,
                                    isLoading = uiState.isConfirmingFee,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = order.farmerName ?: order.counterpartyName ?: "Farmer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "Phone: ${order.farmerPhone ?: "-"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Order items",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            order.items.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = item.productName,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Text(
                                            text = "${formatMoney(item.orderedQuantity)} ${item.unitType.toDisplayLabel()} × BDT ${formatMoney(item.unitPrice)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = "BDT ${formatMoney(item.orderedQuantity * item.unitPrice)}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (index != order.items.lastIndex) {
                                    Spacer(modifier = Modifier.size(6.dp))
                                }
                            }

                            Spacer(modifier = Modifier.size(4.dp))

                            SummaryRow(
                                label = "Estimated total",
                                value = "BDT ${formatMoney(order.estimatedProductTotal)}"
                            )

                            SummaryRow(
                                label = "Service fee",
                                value = "BDT ${formatMoney(order.serviceFeeAmount)}"
                            )

                            if (order.actualProductTotal != null) {
                                SummaryRow(
                                    label = "Actual total",
                                    value = "BDT ${formatMoney(order.actualProductTotal)}"
                                )
                            }

                            if (order.feePaidAt.isNullOrBlank()) {
                                Text(
                                    text = "Commission pending",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Text(
                                    text = "Commission paid",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFF18D66B)
                                )
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Pickup location",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = listOfNotNull(order.pickupArea, order.pickupCity)
                                    .joinToString(", ")
                                    .ifBlank { "-" },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = order.pickupAddress ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (!order.pickupInstructions.isNullOrBlank()) {
                                Text(
                                    text = "Instructions: ${order.pickupInstructions}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = "Pickup due: ${order.pickupDueAt}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (!order.pickupCode.isNullOrBlank()) {
                                Text(
                                    text = "Pickup code: ${order.pickupCode}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFF18D66B)
                                )
                            }
                        }
                    }

                    uiState.actionErrorMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    uiState.actionSuccessMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF18D66B)
                        )
                    }

                    if (order.status == OrderStatus.Pending || order.status == OrderStatus.AwaitingFee) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Cancel order",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                OutlinedTextField(
                                    value = uiState.cancelNote,
                                    onValueChange = onCancelNoteChanged,
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Optional note") },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF18D66B),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                FarmGateSecondaryButton(
                                    text = if (uiState.isCancelling) "Cancelling..." else "Cancel Order",
                                    onClick = onCancelOrderClick,
                                    enabled = !uiState.isCancelling,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )
                            }
                        }
                    }

                    if (order.status == OrderStatus.Completed) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "After pickup",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "You can now rate the farmer or report an issue for this completed order.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                FarmGatePrimaryButton(
                                    text = "Rate Farmer",
                                    onClick = { onRateFarmerClick(order.id) },
                                    enabled = true,
                                    isLoading = false,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )

                                FarmGateSecondaryButton(
                                    text = "Report Issue",
                                    onClick = { onReportIssueClick(order.id) },
                                    enabled = true,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatusChip(
    text: String,
    background: Color,
    content: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = background
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = content
        )
    }
}

data class OrderStatusUi(
    val label: String,
    val title: String,
    val background: Color,
    val content: Color,
    val description: (Order) -> String
)

private fun OrderStatus.toUi(): OrderStatusUi {
    return when (this) {
        OrderStatus.Pending -> OrderStatusUi(
            label = "PENDING",
            title = "Waiting for farmer response",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B),
            description = { "Your order has been placed and is waiting for the farmer to review it." }
        )
        OrderStatus.AwaitingFee -> OrderStatusUi(
            label = "AWAITING FEE",
            title = "Commission payment required",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316),
            description = { "The farmer accepted your order. Please confirm the platform service fee to continue." }
        )
        OrderStatus.Confirmed -> OrderStatusUi(
            label = "READY FOR PICKUP",
            title = "Ready for pickup",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B),
            description = { order ->
                "Your items are confirmed. Please collect them before ${order.pickupDueAt}."
            }
        )
        OrderStatus.Completed -> OrderStatusUi(
            label = "COMPLETED",
            title = "Order completed",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6),
            description = { "This order has been completed successfully." }
        )
        OrderStatus.Cancelled -> OrderStatusUi(
            label = "CANCELLED",
            title = "Order cancelled",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            description = { "This order was cancelled." }
        )
        OrderStatus.Rejected -> OrderStatusUi(
            label = "REJECTED",
            title = "Order rejected",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            description = { "The farmer rejected this order." }
        )
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

private fun formatMoney(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}