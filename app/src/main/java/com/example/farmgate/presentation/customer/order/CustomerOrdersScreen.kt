package com.example.farmgate.presentation.customer.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderStatus
import java.util.Locale

private enum class OrderTab {
    Active,
    Completed,
    Cancelled
}

@Composable
fun CustomerOrdersScreen(
    uiState: CustomerOrdersUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onOrderClick: (Long) -> Unit
) {
    var selectedTab by remember { mutableStateOf(OrderTab.Active) }

    val filteredOrders = remember(uiState.orders, selectedTab) {
        uiState.orders.filter { order ->
            when (selectedTab) {
                OrderTab.Active -> order.status in setOf(
                    OrderStatus.Pending,
                    OrderStatus.AwaitingFee,
                    OrderStatus.Confirmed
                )
                OrderTab.Completed -> order.status == OrderStatus.Completed
                OrderTab.Cancelled -> order.status in setOf(
                    OrderStatus.Cancelled,
                    OrderStatus.Rejected
                )
            }
        }
    }

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
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "My Orders",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(onClick = onRetry) {
                    Text("Retry")
                }

                if (onBackClick !== {}) {
                    TextButton(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Orders",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )


                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OrdersTabChip(
                            text = "Active",
                            selected = selectedTab == OrderTab.Active,
                            onClick = { selectedTab = OrderTab.Active },
                            modifier = Modifier.weight(1f)
                        )
                        OrdersTabChip(
                            text = "Completed",
                            selected = selectedTab == OrderTab.Completed,
                            onClick = { selectedTab = OrderTab.Completed },
                            modifier = Modifier.weight(1f)
                        )
                        OrdersTabChip(
                            text = "Cancelled",
                            selected = selectedTab == OrderTab.Cancelled,
                            onClick = { selectedTab = OrderTab.Cancelled },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (filteredOrders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (selectedTab) {
                                OrderTab.Active -> "No active orders."
                                OrderTab.Completed -> "No completed orders."
                                OrderTab.Cancelled -> "No cancelled orders."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders) { order ->
                            OrderCard(
                                order = order,
                                onClick = { onOrderClick(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersTabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) Color(0x1A18D66B) else MaterialTheme.colorScheme.surface
    val borderColor = if (selected) Color(0xFF18D66B) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val textColor = if (selected) Color(0xFF18D66B) else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = textColor
            )
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    val statusUi = order.status.toUi()
    val locationText = listOfNotNull(order.pickupArea, order.pickupCity)
        .joinToString(", ")
        .ifBlank { "Pickup details available" }

    val commissionText = if (order.feePaidAt.isNullOrBlank()) {
        "Commission pending"
    } else {
        "Commission paid"
    }

    val ctaText = when (order.status) {
        OrderStatus.Pending -> "View Order"
        OrderStatus.AwaitingFee -> "Pay Fee"
        OrderStatus.Confirmed -> "Track Status"
        OrderStatus.Completed -> "View Details"
        OrderStatus.Cancelled -> "View Details"
        OrderStatus.Rejected -> "View Details"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusChip(
                        text = statusUi.label,
                        background = statusUi.background,
                        content = statusUi.content
                    )

                    Text(
                        text = order.counterpartyName
                            ?: order.farmerName
                            ?: "Farmer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = commissionText,
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

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "#${order.id}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Pickup due",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = order.pickupDueAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Estimated total: BDT ${formatMoney(order.estimatedProductTotal)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x1A18D66B)
                ) {
                    Text(
                        text = ctaText,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }
        }
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

private data class OrderStatusUi(
    val label: String,
    val background: Color,
    val content: Color
)

private fun OrderStatus.toUi(): OrderStatusUi {
    return when (this) {
        OrderStatus.Pending -> OrderStatusUi(
            label = "PENDING",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B)
        )
        OrderStatus.AwaitingFee -> OrderStatusUi(
            label = "AWAITING FEE",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316)
        )
        OrderStatus.Confirmed -> OrderStatusUi(
            label = "READY FOR PICKUP",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B)
        )
        OrderStatus.Completed -> OrderStatusUi(
            label = "COMPLETED",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6)
        )
        OrderStatus.Cancelled -> OrderStatusUi(
            label = "CANCELLED",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48)
        )
        OrderStatus.Rejected -> OrderStatusUi(
            label = "REJECTED",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48)
        )
    }
}

private fun formatMoney(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}