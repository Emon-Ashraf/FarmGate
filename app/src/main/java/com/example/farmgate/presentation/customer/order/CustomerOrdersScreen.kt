package com.example.farmgate.presentation.customer.order

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
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

    val activeOrders = remember(uiState.orders) {
        uiState.orders.filter {
            it.status == OrderStatus.Pending ||
                    it.status == OrderStatus.AwaitingFee ||
                    it.status == OrderStatus.Confirmed
        }
    }

    val completedOrders = remember(uiState.orders) {
        uiState.orders.filter { it.status == OrderStatus.Completed }
    }

    val cancelledOrders = remember(uiState.orders) {
        uiState.orders.filter {
            it.status == OrderStatus.Cancelled ||
                    it.status == OrderStatus.Rejected
        }
    }

    val filteredOrders = when (selectedTab) {
        OrderTab.Active -> activeOrders
        OrderTab.Completed -> completedOrders
        OrderTab.Cancelled -> cancelledOrders
    }

    when {
        uiState.isLoading -> {
            OrdersLoadingState()
        }

        uiState.errorMessage != null -> {
            OrdersErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry
            )
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                OrdersHeader(
                    totalOrders = uiState.orders.size,
                    activeCount = activeOrders.size
                )

                OrdersTabs(
                    selectedTab = selectedTab,
                    activeCount = activeOrders.size,
                    completedCount = completedOrders.size,
                    cancelledCount = cancelledOrders.size,
                    onTabSelected = { selectedTab = it }
                )

                if (filteredOrders.isEmpty()) {
                    EmptyOrdersState(selectedTab = selectedTab)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 18.dp,
                            end = 18.dp,
                            top = 14.dp,
                            bottom = 120.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredOrders,
                            key = { order -> order.id }
                        ) { order ->
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
private fun OrdersLoadingState() {
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
private fun OrdersErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "My orders",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun OrdersHeader(
    totalOrders: Int,
    activeCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "My orders",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = when {
                totalOrders == 0 -> "You have not placed any orders yet."
                activeCount > 0 -> "$activeCount active ${if (activeCount == 1) "order" else "orders"} to follow"
                else -> "$totalOrders ${if (totalOrders == 1) "order" else "orders"} in your history"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OrdersTabs(
    selectedTab: OrderTab,
    activeCount: Int,
    completedCount: Int,
    cancelledCount: Int,
    onTabSelected: (OrderTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            OrdersTabChip(
                text = "Active",
                count = activeCount,
                selected = selectedTab == OrderTab.Active,
                onClick = { onTabSelected(OrderTab.Active) },
                modifier = Modifier.weight(1f)
            )

            OrdersTabChip(
                text = "Completed",
                count = completedCount,
                selected = selectedTab == OrderTab.Completed,
                onClick = { onTabSelected(OrderTab.Completed) },
                modifier = Modifier.weight(1f)
            )

            OrdersTabChip(
                text = "Cancelled",
                count = cancelledCount,
                selected = selectedTab == OrderTab.Cancelled,
                onClick = { onTabSelected(OrderTab.Cancelled) },
                modifier = Modifier.weight(1f)
            )

        }
    }
}

@Composable
private fun OrdersTabChip(
    text: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF18D66B)

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (selected) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
                ),
                color = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            if (count > 0) {
                Spacer(modifier = Modifier.size(6.dp))

                Surface(
                    shape = CircleShape,
                    color = if (selected) Color(0x1A18D66B) else MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = if (count > 9) "9+" else count.toString(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyOrdersState(
    selectedTab: OrderTab
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = Color(0x1A18D66B)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_orders),
                            contentDescription = null,
                            tint = Color(0xFF18D66B),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Text(
                    text = when (selectedTab) {
                        OrderTab.Active -> "No active orders"
                        OrderTab.Completed -> "No completed orders"
                        OrderTab.Cancelled -> "No closed orders"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = when (selectedTab) {
                        OrderTab.Active -> "Orders waiting for farmer approval or pickup will appear here."
                        OrderTab.Completed -> "Completed pickup orders will appear here."
                        OrderTab.Cancelled -> "Cancelled or rejected orders will appear here."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    val statusUi = order.status.toListUi()
    val locationText = listOfNotNull(order.pickupArea, order.pickupCity)
        .joinToString(", ")
        .ifBlank { "Pickup location unavailable" }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
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
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OrderNumberBadge(orderId = order.id)
            }

            DetailRow(
                label = "Pickup due",
                value = order.pickupDueAt
            )

            DetailRow(
                label = "Product total",
                value = "BDT ${formatMoney(order.estimatedProductTotal)}"
            )

            DetailRow(
                label = "Service fee",
                value = "BDT ${formatMoney(order.serviceFeeAmount)}"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeeStatusChip(order = order)

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = statusUi.actionBackground
                ) {
                    Text(
                        text = order.status.actionLabel(),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = statusUi.actionContent
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderNumberBadge(
    orderId: Long
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Text(
            text = "#$orderId",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DetailRow(
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
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FeeStatusChip(
    order: Order
) {
    val isPaid = !order.feePaidAt.isNullOrBlank()

    val text = when {
        order.status == OrderStatus.Pending -> "Fee after approval"
        isPaid -> "Fee paid"
        order.status == OrderStatus.AwaitingFee -> "Fee pending"
        else -> "Fee not paid"
    }

    val contentColor = when {
        isPaid -> Color(0xFF18D66B)
        order.status == OrderStatus.Pending -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.error
    }

    val backgroundColor = when {
        isPaid -> Color(0x1A18D66B)
        order.status == OrderStatus.Pending -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        else -> MaterialTheme.colorScheme.errorContainer
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = contentColor
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

private data class OrdersListStatusUi(
    val label: String,
    val background: Color,
    val content: Color,
    val actionBackground: Color,
    val actionContent: Color
)

private fun OrderStatus.toListUi(): OrdersListStatusUi {
    return when (this) {
        OrderStatus.Pending -> OrdersListStatusUi(
            label = "Waiting approval",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B),
            actionBackground = Color(0x1A18D66B),
            actionContent = Color(0xFF18D66B)
        )

        OrderStatus.AwaitingFee -> OrdersListStatusUi(
            label = "Service fee required",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316),
            actionBackground = Color(0x1AF97316),
            actionContent = Color(0xFFF97316)
        )

        OrderStatus.Confirmed -> OrdersListStatusUi(
            label = "Ready for pickup",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B),
            actionBackground = Color(0x1A18D66B),
            actionContent = Color(0xFF18D66B)
        )

        OrderStatus.Completed -> OrdersListStatusUi(
            label = "Completed",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6),
            actionBackground = MaterialThemeSafeSurfaceVariant,
            actionContent = Color(0xFF3B82F6)
        )

        OrderStatus.Cancelled -> OrdersListStatusUi(
            label = "Cancelled",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            actionBackground = Color(0x1AE11D48),
            actionContent = Color(0xFFE11D48)
        )

        OrderStatus.Rejected -> OrdersListStatusUi(
            label = "Rejected",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48),
            actionBackground = Color(0x1AE11D48),
            actionContent = Color(0xFFE11D48)
        )
    }
}

private val MaterialThemeSafeSurfaceVariant = Color(0x1A3B82F6)

private fun OrderStatus.actionLabel(): String {
    return when (this) {
        OrderStatus.Pending -> "View"
        OrderStatus.AwaitingFee -> "Pay fee"
        OrderStatus.Confirmed -> "Pickup"
        OrderStatus.Completed -> "Details"
        OrderStatus.Cancelled -> "Details"
        OrderStatus.Rejected -> "Details"
    }
}

private fun formatMoney(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}