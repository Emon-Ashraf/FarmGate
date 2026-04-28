package com.example.farmgate.presentation.farmer.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.Order
import com.example.farmgate.data.model.OrderStatus
import java.util.Locale

@Composable
fun FarmerHomeScreen(
    farmerName: String,
    isProfileCompleted: Boolean,
    orders: List<Order>,
    onOrdersClick: () -> Unit,
    onProductsClick: () -> Unit,
    onPickupLocationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onOrderClick: (Long) -> Unit,
) {
    val pendingOrders = orders.count { it.status == OrderStatus.Pending }
    val awaitingFeeOrders = orders.count { it.status == OrderStatus.AwaitingFee }
    val pickupReadyOrders = orders.count { it.status == OrderStatus.Confirmed }
    val completedOrders = orders.count { it.status == OrderStatus.Completed }

    val recentOrders = orders
        .filter {
            it.status == OrderStatus.Pending ||
                    it.status == OrderStatus.AwaitingFee ||
                    it.status == OrderStatus.Confirmed
        }
        .take(3)

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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FarmerDashboardHeader(farmerName = farmerName)
        }

        item {
            FarmerProfileStatusCard(
                farmerName = farmerName,
                isProfileCompleted = isProfileCompleted,
                onProfileClick = onProfileClick
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerStatCard(
                    title = "New",
                    value = pendingOrders.toString(),
                    subtitle = "Need response",
                    accentColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )

                FarmerStatCard(
                    title = "Pickup",
                    value = pickupReadyOrders.toString(),
                    subtitle = "Ready orders",
                    accentColor = Color(0xFF18D66B),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerStatCard(
                    title = "Fees",
                    value = awaitingFeeOrders.toString(),
                    subtitle = "Waiting customer",
                    accentColor = Color(0xFFF97316),
                    modifier = Modifier.weight(1f)
                )

                FarmerStatCard(
                    title = "Done",
                    value = completedOrders.toString(),
                    subtitle = "Completed",
                    accentColor = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            FarmerQuickActionsCard(
                onOrdersClick = onOrdersClick,
                onProductsClick = onProductsClick,
                onPickupLocationsClick = onPickupLocationsClick,
                onProfileClick = onProfileClick
            )
        }

        item {
            SectionHeader(
                title = "Needs attention",
                actionText = "View all",
                onActionClick = onOrdersClick
            )
        }

        if (recentOrders.isEmpty()) {
            item {
                EmptyFarmerOrdersCard()
            }
        } else {
            items(
                items = recentOrders,
                key = { order -> order.id }
            ) { order ->
                FarmerRecentOrderCard(
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
private fun FarmerDashboardHeader(
    farmerName: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Farmer dashboard",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Welcome back, ${farmerName.ifBlank { "Farmer" }}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FarmerProfileStatusCard(
    farmerName: String,
    isProfileCompleted: Boolean,
    onProfileClick: () -> Unit
) {
    val initial = farmerName.firstOrNull()?.uppercaseChar()?.toString() ?: "F"

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
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(62.dp),
                shape = CircleShape,
                color = if (isProfileCompleted) Color(0x1A18D66B) else MaterialTheme.colorScheme.errorContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = if (isProfileCompleted) Color(0xFF18D66B) else MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = if (isProfileCompleted) "Profile ready" else "Complete your profile",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isProfileCompleted) {
                        "You can manage pickup locations, products, and customer orders."
                    } else {
                        "Complete your farmer profile before creating products."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                onClick = onProfileClick,
                shape = RoundedCornerShape(999.dp),
                color = if (isProfileCompleted) Color(0x1A18D66B) else MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = if (isProfileCompleted) "Edit" else "Complete",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isProfileCompleted) Color(0xFF18D66B) else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun FarmerStatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = title.first().toString(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = accentColor
                    )
                }
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FarmerQuickActionsCard(
    onOrdersClick: () -> Unit,
    onProductsClick: () -> Unit,
    onPickupLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
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
                text = "Quick actions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FarmerActionChip(
                    text = "Orders",
                    onClick = onOrdersClick,
                    modifier = Modifier.weight(1f)
                )

                FarmerActionChip(
                    text = "Products",
                    onClick = onProductsClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FarmerActionChip(
                    text = "Pickup",
                    onClick = onPickupLocationsClick,
                    modifier = Modifier.weight(1f)
                )

                FarmerActionChip(
                    text = "Profile",
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FarmerActionChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color(0x1A18D66B)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF18D66B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Surface(
            onClick = onActionClick,
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = actionText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF18D66B)
            )
        }
    }
}

@Composable
private fun EmptyFarmerOrdersCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "No urgent orders",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "New requests and pickup-ready orders will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerRecentOrderCard(
    order: Order,
    onClick: () -> Unit
) {
    val statusUi = order.status.toDashboardUi()
    val customerName = order.customerName ?: order.counterpartyName ?: "Customer"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
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
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    StatusChip(
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
                        text = listOfNotNull(order.pickupArea, order.pickupCity)
                            .joinToString(", ")
                            .ifBlank { "Pickup location unavailable" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
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
                }

                Text(
                    text = "BDT ${formatMoney(order.estimatedProductTotal)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
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

private data class DashboardOrderStatusUi(
    val label: String,
    val background: Color,
    val content: Color
)

private fun OrderStatus.toDashboardUi(): DashboardOrderStatusUi {
    return when (this) {
        OrderStatus.Pending -> DashboardOrderStatusUi(
            label = "New request",
            background = Color(0x1AF59E0B),
            content = Color(0xFFF59E0B)
        )

        OrderStatus.AwaitingFee -> DashboardOrderStatusUi(
            label = "Waiting fee",
            background = Color(0x1AF97316),
            content = Color(0xFFF97316)
        )

        OrderStatus.Confirmed -> DashboardOrderStatusUi(
            label = "Ready pickup",
            background = Color(0x1A18D66B),
            content = Color(0xFF18D66B)
        )

        OrderStatus.Completed -> DashboardOrderStatusUi(
            label = "Completed",
            background = Color(0x1A3B82F6),
            content = Color(0xFF3B82F6)
        )

        OrderStatus.Cancelled -> DashboardOrderStatusUi(
            label = "Cancelled",
            background = Color(0x1AE11D48),
            content = Color(0xFFE11D48)
        )

        OrderStatus.Rejected -> DashboardOrderStatusUi(
            label = "Rejected",
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