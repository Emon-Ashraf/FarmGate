package com.example.farmgate.presentation.farmer.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderItem
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
            FarmerOrderDetailsLoadingState()
        }

        uiState.errorMessage != null -> {
            FarmerOrderDetailsErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry,
                onBackClick = onBackClick
            )
        }

        uiState.order != null -> {
            FarmerOrderDetailsContent(
                uiState = uiState,
                order = uiState.order,
                onBackClick = onBackClick,
                onAcceptClick = onAcceptClick,
                onRejectClick = onRejectClick,
                onPickupCodeChanged = onPickupCodeChanged,
                onFulfilledQuantityChanged = onFulfilledQuantityChanged,
                onCompleteClick = onCompleteClick
            )
        }
    }
}

@Composable
private fun FarmerOrderDetailsLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FarmerOrderDetailsErrorState(
    message: String,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Order details",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = message,
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

@Composable
private fun FarmerOrderDetailsContent(
    uiState: FarmerOrderDetailsUiState,
    order: Order,
    onBackClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    onPickupCodeChanged: (String) -> Unit,
    onFulfilledQuantityChanged: (Long, String) -> Unit,
    onCompleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FarmerOrderTopBar(
            orderId = order.id,
            onBackClick = onBackClick
        )

        FarmerOrderStatusHero(order = order)

        uiState.actionErrorMessage?.let { message ->
            FarmerOrderMessageCard(
                message = message,
                isError = true
            )
        }

        uiState.actionSuccessMessage?.let { message ->
            FarmerOrderMessageCard(
                message = message,
                isError = false
            )
        }

        FarmerCustomerCard(order = order)

        FarmerOrderItemsCard(order = order)

        FarmerPickupCard(order = order)

        when (order.status) {
            OrderStatus.Pending -> {
                FarmerReviewRequestCard(
                    isAccepting = uiState.isAccepting,
                    isRejecting = uiState.isRejecting,
                    onAcceptClick = onAcceptClick,
                    onRejectClick = onRejectClick
                )
            }

            OrderStatus.Confirmed -> {
                FarmerCompletePickupCard(
                    order = order,
                    pickupCode = uiState.pickupCode,
                    fulfilledQuantities = uiState.fulfilledQuantities,
                    isCompleting = uiState.isCompleting,
                    onPickupCodeChanged = onPickupCodeChanged,
                    onFulfilledQuantityChanged = onFulfilledQuantityChanged,
                    onCompleteClick = onCompleteClick
                )
            }

            OrderStatus.AwaitingFee -> {
                FarmerWaitingFeeCard()
            }

            OrderStatus.Completed,
            OrderStatus.Cancelled,
            OrderStatus.Rejected -> {
                FarmerClosedOrderCard(status = order.status)
            }
        }

        Spacer(modifier = Modifier.navigationBarsPadding())
        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun FarmerOrderTopBar(
    orderId: Long,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Order #$orderId",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Farmer order management",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerOrderStatusHero(
    order: Order
) {
    val statusUi = order.status.toFarmerDetailUi()

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FarmerDetailStatusChip(
                text = statusUi.label,
                background = statusUi.background,
                content = statusUi.content
            )

            Text(
                text = statusUi.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = statusUi.description(order),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FarmerMiniInfoBlock(
                    label = "Estimated total",
                    value = "BDT ${formatMoney(order.estimatedProductTotal)}",
                    modifier = Modifier.weight(1f)
                )

                FarmerMiniInfoBlock(
                    label = "Service fee",
                    value = "BDT ${formatMoney(order.serviceFeeAmount)}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FarmerMiniInfoBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FarmerCustomerCard(
    order: Order
) {
    val customerName = order.customerName ?: order.counterpartyName ?: "Customer"
    val initial = customerName.firstOrNull()?.uppercaseChar()?.toString() ?: "C"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0x1A18D66B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Customer",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = customerName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FarmerOrderItemsCard(
    order: Order
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Order items",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            order.items.forEachIndexed { index, item ->
                FarmerOrderItemRow(item = item)

                if (index != order.items.lastIndex) {
                    FarmerDivider()
                }
            }

            FarmerDivider()

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
                    label = "Actual product total",
                    value = "BDT ${formatMoney(order.actualProductTotal)}"
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (order.feePaidAt.isNullOrBlank()) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    Color(0x1A18D66B)
                }
            ) {
                Text(
                    text = if (order.feePaidAt.isNullOrBlank()) {
                        "Commission not confirmed yet"
                    } else {
                        "Commission confirmed"
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (order.feePaidAt.isNullOrBlank()) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        Color(0xFF18D66B)
                    }
                )
            }
        }
    }
}

@Composable
private fun FarmerOrderItemRow(
    item: OrderItem
) {
    val unitLabel = item.unitType.toDisplayLabel()
    val fulfilledText = item.fulfilledQuantity?.let {
        "Fulfilled: ${formatQuantity(it, item.unitType)} $unitLabel"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0x1A18D66B)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = item.productName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF18D66B)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${formatQuantity(item.orderedQuantity, item.unitType)} $unitLabel × BDT ${formatMoney(item.unitPrice)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (fulfilledText != null) {
                Text(
                    text = fulfilledText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF18D66B)
                )
            }
        }

        Text(
            text = "BDT ${formatMoney(item.orderedQuantity * item.unitPrice)}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun FarmerPickupCard(
    order: Order
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Pickup details",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            FarmerSummaryRow(
                label = "Location",
                value = listOfNotNull(order.pickupArea, order.pickupCity)
                    .joinToString(", ")
                    .ifBlank { "-" }
            )

            FarmerSummaryRow(
                label = "Address",
                value = order.pickupAddress ?: "-"
            )

            FarmerSummaryRow(
                label = "Pickup due",
                value = order.pickupDueAt
            )

            if (!order.pickupInstructions.isNullOrBlank()) {
                Text(
                    text = "Instructions: ${order.pickupInstructions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!order.pickupCode.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x1A18D66B)
                ) {
                    Text(
                        text = "Pickup code: ${order.pickupCode}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }
        }
    }
}

@Composable
private fun FarmerReviewRequestCard(
    isAccepting: Boolean,
    isRejecting: Boolean,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Review request",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Accept this order only if you can prepare the items for pickup. Reject it if products are unavailable.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FarmGatePrimaryButton(
                text = if (isAccepting) "Accepting..." else "Accept order",
                onClick = onAcceptClick,
                enabled = !isAccepting && !isRejecting,
                isLoading = isAccepting,
                modifier = Modifier.heightIn(min = 50.dp)
            )

            FarmGateSecondaryButton(
                text = if (isRejecting) "Rejecting..." else "Reject order",
                onClick = onRejectClick,
                enabled = !isAccepting && !isRejecting,
                modifier = Modifier.heightIn(min = 50.dp)
            )
        }
    }
}

@Composable
private fun FarmerWaitingFeeCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0x1AF97316)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Waiting for customer",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFFB45309)
            )

            Text(
                text = "You accepted this order. The customer must confirm the platform service fee before pickup can be completed.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB45309)
            )
        }
    }
}

@Composable
private fun FarmerCompletePickupCard(
    order: Order,
    pickupCode: String,
    fulfilledQuantities: Map<Long, String>,
    isCompleting: Boolean,
    onPickupCodeChanged: (String) -> Unit,
    onFulfilledQuantityChanged: (Long, String) -> Unit,
    onCompleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
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
                text = "Verify the customer pickup code and final fulfilled quantities before completing the order.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = pickupCode,
                onValueChange = onPickupCodeChanged,
                label = { Text("Pickup code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF18D66B),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
                )
            )

            order.items.forEach { item ->
                OutlinedTextField(
                    value = fulfilledQuantities[item.id].orEmpty(),
                    onValueChange = { onFulfilledQuantityChanged(item.id, it) },
                    label = {
                        Text("Fulfilled - ${item.productName}")
                    },
                    supportingText = {
                        Text(
                            text = "Ordered: ${formatQuantity(item.orderedQuantity, item.unitType)} ${item.unitType.toDisplayLabel()}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF18D66B),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
                    )
                )
            }

            FarmGatePrimaryButton(
                text = if (isCompleting) "Completing..." else "Complete order",
                onClick = onCompleteClick,
                enabled = !isCompleting,
                isLoading = isCompleting,
                modifier = Modifier.heightIn(min = 50.dp)
            )
        }
    }
}

@Composable
private fun FarmerClosedOrderCard(
    status: OrderStatus
) {
    val text = when (status) {
        OrderStatus.Completed -> "This order is completed. No further farmer action is required."
        OrderStatus.Cancelled -> "This order was cancelled. No further farmer action is required."
        OrderStatus.Rejected -> "This order was rejected. No further farmer action is required."
        else -> "No further farmer action is required."
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Order closed",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerOrderMessageCard(
    message: String,
    isError: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = if (isError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color(0x1A18D66B)
        }
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = if (isError) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                Color(0xFF18D66B)
            }
        )
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
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.9f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            modifier = Modifier.weight(1.2f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
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
        shape = RoundedCornerShape(999.dp),
        color = background
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = content
        )
    }
}

@Composable
private fun FarmerDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    )
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