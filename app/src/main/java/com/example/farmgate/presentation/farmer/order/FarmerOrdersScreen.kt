package com.example.farmgate.presentation.farmer.order

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderStatus
import java.util.Locale

private enum class FarmerOrderTab {
    New,
    Active,
    Finished
}

@Composable
fun FarmerOrdersScreen(
    uiState: FarmerOrdersUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onOrderClick: (Long) -> Unit
) {
    var selectedTab by remember { mutableStateOf(FarmerOrderTab.New) }

    val newCount = uiState.orders.count { it.status == OrderStatus.Pending }
    val activeCount = uiState.orders.count {
        it.status == OrderStatus.AwaitingFee || it.status == OrderStatus.Confirmed
    }
    val finishedCount = uiState.orders.count {
        it.status == OrderStatus.Completed ||
                it.status == OrderStatus.Cancelled ||
                it.status == OrderStatus.Rejected
    }

    val filteredOrders = remember(uiState.orders, selectedTab) {
        uiState.orders.filter { order ->
            when (selectedTab) {
                FarmerOrderTab.New -> order.status == OrderStatus.Pending
                FarmerOrderTab.Active -> order.status in setOf(
                    OrderStatus.AwaitingFee,
                    OrderStatus.Confirmed
                )
                FarmerOrderTab.Finished -> order.status in setOf(
                    OrderStatus.Completed,
                    OrderStatus.Cancelled,
                    OrderStatus.Rejected
                )
            }
        }
    }

    when {
        uiState.isLoading -> {
            FarmerOrdersLoadingState()
        }

        uiState.errorMessage != null -> {
            FarmerOrdersErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry,
                onBackClick = onBackClick
            )
        }

        else -> {
            FarmerOrdersContent(
                selectedTab = selectedTab,
                newCount = newCount,
                activeCount = activeCount,
                finishedCount = finishedCount,
                filteredOrders = filteredOrders,
                onTabSelected = { selectedTab = it },
                onOrderClick = onOrderClick
            )
        }
    }
}

@Composable
private fun FarmerOrdersLoadingState() {
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
private fun FarmerOrdersErrorState(
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
            text = "Farmer orders",
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

        TextButton(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Composable
private fun FarmerOrdersContent(
    selectedTab: FarmerOrderTab,
    newCount: Int,
    activeCount: Int,
    finishedCount: Int,
    filteredOrders: List<Order>,
    onTabSelected: (FarmerOrderTab) -> Unit,
    onOrderClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 16.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            FarmerOrdersHeader()
        }

        item {
            FarmerOrdersSummaryCard(
                newCount = newCount,
                activeCount = activeCount,
                finishedCount = finishedCount
            )
        }

        item {
            FarmerOrderTabs(
                selectedTab = selectedTab,
                newCount = newCount,
                activeCount = activeCount,
                finishedCount = finishedCount,
                onTabSelected = onTabSelected
            )
        }

        if (filteredOrders.isEmpty()) {
            item {
                FarmerOrdersEmptyState(selectedTab = selectedTab)
            }
        } else {
            items(
                items = filteredOrders,
                key = { order -> order.id }
            ) { order ->
                FarmerOrderCard(
                    order = order,
                    onClick = { onOrderClick(order.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun FarmerOrdersHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Orders",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Review requests, track pickup readiness, and complete confirmed orders.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FarmerOrdersSummaryCard(
    newCount: Int,
    activeCount: Int,
    finishedCount: Int
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FarmerOrderSummaryItem(
                label = "New",
                value = newCount.toString(),
                accentColor = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )

            FarmerOrderSummaryItem(
                label = "Active",
                value = activeCount.toString(),
                accentColor = Color(0xFF18D66B),
                modifier = Modifier.weight(1f)
            )

            FarmerOrderSummaryItem(
                label = "Finished",
                value = finishedCount.toString(),
                accentColor = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FarmerOrderSummaryItem(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = accentColor.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = accentColor
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun FarmerOrderTabs(
    selectedTab: FarmerOrderTab,
    newCount: Int,
    activeCount: Int,
    finishedCount: Int,
    onTabSelected: (FarmerOrderTab) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FarmerOrdersTabChip(
            text = "New",
            count = newCount,
            selected = selectedTab == FarmerOrderTab.New,
            onClick = { onTabSelected(FarmerOrderTab.New) },
            modifier = Modifier.weight(1f)
        )

        FarmerOrdersTabChip(
            text = "Active",
            count = activeCount,
            selected = selectedTab == FarmerOrderTab.Active,
            onClick = { onTabSelected(FarmerOrderTab.Active) },
            modifier = Modifier.weight(1f)
        )

        FarmerOrdersTabChip(
            text = "Finished",
            count = finishedCount,
            selected = selectedTab == FarmerOrderTab.Finished,
            onClick = { onTabSelected(FarmerOrderTab.Finished) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FarmerOrdersTabChip(
    text: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFF18D66B)
    val background = if (selected) Color(0x1A18D66B) else MaterialTheme.colorScheme.surface
    val borderColor = if (selected) selectedColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
    val textColor = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = background,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = textColor,
                maxLines = 1
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = textColor.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun FarmerOrdersEmptyState(
    selectedTab: FarmerOrderTab
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
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when (selectedTab) {
                    FarmerOrderTab.New -> "No new requests"
                    FarmerOrderTab.Active -> "No active orders"
                    FarmerOrderTab.Finished -> "No finished orders"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = when (selectedTab) {
                    FarmerOrderTab.New -> "Customer requests waiting for farmer approval will appear here."
                    FarmerOrderTab.Active -> "Accepted orders waiting for fee confirmation or pickup completion will appear here."
                    FarmerOrderTab.Finished -> "Completed, cancelled, and rejected orders will appear here."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerOrderCard(
    order: Order,
    onClick: () -> Unit
) {
    val statusUi = order.status.toFarmerListUi()
    val customerName = order.counterpartyName ?: order.customerName ?: "Customer"
    val locationText = listOfNotNull(order.pickupArea, order.pickupCity)
        .joinToString(", ")
        .ifBlank { "Pickup details available" }

    val ctaText = when (order.status) {
        OrderStatus.Pending -> "Review"
        OrderStatus.AwaitingFee -> "Waiting fee"
        OrderStatus.Confirmed -> "Complete"
        OrderStatus.Completed -> "Details"
        OrderStatus.Cancelled -> "Details"
        OrderStatus.Rejected -> "Details"
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    FarmerStatusChip(
                        text = statusUi.label,
                        background = statusUi.background,
                        content = statusUi.content
                    )

                    Text(
                        text = customerName,
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

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                ) {
                    Text(
                        text = "#${order.id}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            FarmerOrderCardDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FarmerOrderInfoBlock(
                    label = "Pickup due",
                    value = order.pickupDueAt,
                    modifier = Modifier.weight(1f)
                )

                FarmerOrderInfoBlock(
                    label = "Total",
                    value = "BDT ${formatMoney(order.estimatedProductTotal)}",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommissionStatusText(order = order)

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color(0x1A18D66B)
                ) {
                    Text(
                        text = ctaText,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF18D66B),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun FarmerOrderInfoBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
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
                style = MaterialTheme.typography.bodySmall.copy(
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
private fun CommissionStatusText(
    order: Order
) {
    val isFeePaid = !order.feePaidAt.isNullOrBlank()

    Text(
        text = if (isFeePaid) {
            "Commission confirmed"
        } else {
            when (order.status) {
                OrderStatus.Pending -> "Not accepted yet"
                OrderStatus.Rejected,
                OrderStatus.Cancelled -> "No fee needed"
                else -> "Commission pending"
            }
        },
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
        color = when {
            isFeePaid -> Color(0xFF18D66B)
            order.status == OrderStatus.Pending -> MaterialTheme.colorScheme.onSurfaceVariant
            order.status == OrderStatus.Rejected || order.status == OrderStatus.Cancelled -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> MaterialTheme.colorScheme.error
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FarmerStatusChip(
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
private fun FarmerOrderCardDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    )
}

private data class FarmerOrdersStatusUi(
    val label: String,
    val background: Color,
    val content: Color
)

private fun OrderStatus.toFarmerListUi(): FarmerOrdersStatusUi {
    return when (this) {
        OrderStatus.Pending -> FarmerOrdersStatusUi(
            label = "NEW REQUEST",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B)
        )

        OrderStatus.AwaitingFee -> FarmerOrdersStatusUi(
            label = "AWAITING FEE",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316)
        )

        OrderStatus.Confirmed -> FarmerOrdersStatusUi(
            label = "READY FOR PICKUP",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B)
        )

        OrderStatus.Completed -> FarmerOrdersStatusUi(
            label = "COMPLETED",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6)
        )

        OrderStatus.Cancelled -> FarmerOrdersStatusUi(
            label = "CANCELLED",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48)
        )

        OrderStatus.Rejected -> FarmerOrdersStatusUi(
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