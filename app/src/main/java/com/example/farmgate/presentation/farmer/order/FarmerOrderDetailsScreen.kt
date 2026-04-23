package com.example.farmgate.presentation.farmer.order

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
            val statusUi = order.status.toFarmerDetailUi()

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
                            FarmerDetailStatusChip(
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
                                text = "Customer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = order.customerName ?: order.counterpartyName ?: "Customer",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
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
                                val fulfilledText = item.fulfilledQuantity?.let {
                                    "Fulfilled: ${formatQuantity(it, item.unitType)} ${item.unitType.toDisplayLabel()}"
                                }

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
                                            text = "Ordered: ${formatQuantity(item.orderedQuantity, item.unitType)} ${item.unitType.toDisplayLabel()} × BDT ${formatMoney(item.unitPrice)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        if (fulfilledText != null) {
                                            Text(
                                                text = fulfilledText,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF18D66B)
                                            )
                                        }
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

                            FarmerSummaryRow(
                                label = "Estimated total",
                                value = "BDT ${formatMoney(order.estimatedProductTotal)}"
                            )

                            FarmerSummaryRow(
                                label = "Service fee",
                                value = "BDT ${formatMoney(order.serviceFeeAmount)}"
                            )

                            if (order.actualProductTotal != null) {
                                FarmerSummaryRow(
                                    label = "Actual total",
                                    value = "BDT ${formatMoney(order.actualProductTotal)}"
                                )
                            }

                            Text(
                                text = if (order.feePaidAt.isNullOrBlank()) {
                                    "Commission not confirmed yet"
                                } else {
                                    "Commission confirmed"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = if (order.feePaidAt.isNullOrBlank()) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    Color(0xFF18D66B)
                                }
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
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Pickup details",
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    uiState.actionSuccessMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFF18D66B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (order.status == OrderStatus.Pending) {
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
                                    text = "Review request",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "Accept this order if you can prepare it for pickup, or reject it if unavailable.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                FarmGatePrimaryButton(
                                    text = if (uiState.isAccepting) "Accepting..." else "Accept Order",
                                    onClick = onAcceptClick,
                                    enabled = !uiState.isAccepting && !uiState.isRejecting,
                                    isLoading = uiState.isAccepting,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )

                                FarmGateSecondaryButton(
                                    text = if (uiState.isRejecting) "Rejecting..." else "Reject Order",
                                    onClick = onRejectClick,
                                    enabled = !uiState.isAccepting && !uiState.isRejecting,
                                    modifier = Modifier.heightIn(min = 50.dp)
                                )
                            }
                        }
                    }

                    if (order.status == OrderStatus.Confirmed) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Complete pickup",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "Enter the customer pickup code and final fulfilled quantities to complete this order.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                OutlinedTextField(
                                    value = uiState.pickupCode,
                                    onValueChange = onPickupCodeChanged,
                                    label = { Text("Pickup Code") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF18D66B),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                order.items.forEach { item ->
                                    OutlinedTextField(
                                        value = uiState.fulfilledQuantities[item.id].orEmpty(),
                                        onValueChange = { onFulfilledQuantityChanged(item.id, it) },
                                        label = {
                                            Text("Fulfilled - ${item.productName}")
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Decimal
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF18D66B),
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }

                                FarmGatePrimaryButton(
                                    text = if (uiState.isCompleting) "Completing..." else "Complete Order",
                                    onClick = onCompleteClick,
                                    enabled = !uiState.isCompleting,
                                    isLoading = uiState.isCompleting,
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
private fun FarmerSummaryRow(
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
private fun FarmerDetailStatusChip(
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

private data class FarmerOrderDetailStatusUi(
    val label: String,
    val title: String,
    val background: Color,
    val content: Color,
    val description: (Order) -> String
)

private fun OrderStatus.toFarmerDetailUi(): FarmerOrderDetailStatusUi {
    return when (this) {
        OrderStatus.Pending -> FarmerOrderDetailStatusUi(
            label = "NEW REQUEST",
            title = "New order request",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B),
            description = { "Review this request and decide whether to accept or reject it." }
        )
        OrderStatus.AwaitingFee -> FarmerOrderDetailStatusUi(
            label = "AWAITING FEE",
            title = "Waiting for customer fee confirmation",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316),
            description = { "The order is accepted. Waiting for the customer to confirm the platform service fee." }
        )
        OrderStatus.Confirmed -> FarmerOrderDetailStatusUi(
            label = "READY FOR PICKUP",
            title = "Prepare for pickup completion",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B),
            description = { order ->
                "This order is ready. Complete it after verifying the pickup code before ${order.pickupDueAt}."
            }
        )
        OrderStatus.Completed -> FarmerOrderDetailStatusUi(
            label = "COMPLETED",
            title = "Order completed",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6),
            description = { "This pickup was completed successfully." }
        )
        OrderStatus.Cancelled -> FarmerOrderDetailStatusUi(
            label = "CANCELLED",
            title = "Order cancelled",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            description = { "This order was cancelled and no further action is needed." }
        )
        OrderStatus.Rejected -> FarmerOrderDetailStatusUi(
            label = "REJECTED",
            title = "Order rejected",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            description = { "This order was rejected and no further action is needed." }
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

private fun formatQuantity(value: Double, unitType: UnitType): String {
    val wholeNumberOnly = unitType == UnitType.Piece ||
            unitType == UnitType.Dozen ||
            unitType == UnitType.Bundle

    return if (wholeNumberOnly) {
        value.toInt().toString()
    } else {
        if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.2f", value)
        }
    }
}