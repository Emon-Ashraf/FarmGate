package com.example.farmgate.presentation.customer.order

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.data.model.OrderDraft
import com.example.farmgate.data.model.OrderDraftItem
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import java.util.Locale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ReviewOrderScreen(
    uiState: ReviewOrderUiState,
    onBackClick: () -> Unit,
    onQuantityChanged: (Long, String) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onClearDraft: () -> Unit,
    onSubmitOrder: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    val draft = uiState.draft

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 16.dp,
                bottom = if (draft == null) 24.dp else 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ReviewOrderTopBar(
                    onBackClick = onBackClick,
                    itemCount = draft?.items?.size ?: 0
                )
            }

            if (draft == null) {
                item {
                    EmptyDraftState(
                        onBackClick = onBackClick
                    )
                }
            } else {
                item {
                    PickupSummaryCard(draft = draft)
                }

                item {
                    SectionTitle(
                        title = "Your items",
                        subtitle = "${draft.items.size} ${if (draft.items.size == 1) "product" else "products"} from this pickup location"
                    )
                }

                items(
                    items = draft.items,
                    key = { item -> item.productId }
                ) { item ->
                    DraftItemCard(
                        item = item,
                        onQuantityChanged = onQuantityChanged,
                        onRemoveItem = onRemoveItem
                    )
                }

                item {
                    SummaryCard(draft = draft)
                }

                uiState.errorMessage?.let { message ->
                    item {
                        ErrorMessageCard(message = message)
                    }
                }
            }
        }

        if (draft != null) {
            ReviewOrderBottomBar(
                isSubmitting = uiState.isSubmitting,
                totalText = "BDT ${formatNumber(draft.estimatedProductTotal)}",
                onSubmitOrder = onSubmitOrder,
                onClearDraft = onClearDraft
            )
        }
    }
}

@Composable
private fun ReviewOrderTopBar(
    onBackClick: () -> Unit,
    itemCount: Int
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
                text = "Review order",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (itemCount == 0) {
                    "Your cart is empty"
                } else {
                    "$itemCount ${if (itemCount == 1) "item" else "items"} in draft"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyDraftState(
    onBackClick: () -> Unit
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color(0x1A18D66B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cart),
                        contentDescription = null,
                        tint = Color(0xFF18D66B),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = "Your order draft is empty",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Add products from the same pickup location to create an order.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            TextButton(onClick = onBackClick) {
                Text("Continue browsing")
            }
        }
    }
}

@Composable
private fun PickupSummaryCard(
    draft: OrderDraft
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color(0x1AF50153)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        tint = Color(0xFFF50153),
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Pickup summary",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = draft.farmerName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${draft.pickupArea}, ${draft.cityName}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = draft.pickupAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DraftItemCard(
    item: OrderDraftItem,
    onQuantityChanged: (Long, String) -> Unit,
    onRemoveItem: (Long) -> Unit
) {
    val unitLabel = item.unitType.toDisplayLabel()
    val itemTotal = item.pricePerUnit * item.selectedQuantity

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!item.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.productName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = item.productName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "BDT ${formatNumber(item.pricePerUnit)} / $unitLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Available: ${formatNumber(item.availableQuantity)} $unitLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "BDT ${formatNumber(itemTotal)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    TextButton(
                        onClick = { onRemoveItem(item.productId) },
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = "Remove",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            QuantityEditor(
                item = item,
                unitLabel = unitLabel,
                onQuantityChanged = onQuantityChanged
            )
        }
    }
}

@Composable
private fun QuantityEditor(
    item: OrderDraftItem,
    unitLabel: String,
    onQuantityChanged: (Long, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityAdjustButton(
            text = "−",
            onClick = {
                val current = item.selectedQuantity
                val next = if (current > 1.0) current - 1.0 else 1.0
                onQuantityChanged(
                    item.productId,
                    formatQuantityInput(next, item.unitType)
                )
            }
        )

        OutlinedTextField(
            value = formatQuantityInput(item.selectedQuantity, item.unitType),
            onValueChange = { onQuantityChanged(item.productId, it) },
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp),
            singleLine = true,
            label = { Text("Quantity ($unitLabel)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF18D66B),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        QuantityAdjustButton(
            text = "+",
            onClick = {
                val next = item.selectedQuantity + 1.0
                onQuantityChanged(
                    item.productId,
                    formatQuantityInput(next, item.unitType)
                )
            }
        )
    }
}

@Composable
private fun SummaryCard(
    draft: OrderDraft
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
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
            Text(
                text = "Order summary",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            SummaryRow(
                label = "Product total",
                value = "BDT ${formatNumber(draft.estimatedProductTotal)}",
                strong = true
            )

            InfoNote(
                text = "Product payment is made directly to the farmer at pickup."
            )

            InfoNote(
                text = "The in-app service fee is handled after the farmer accepts the order."
            )
        }
    }
}

@Composable
private fun InfoNote(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorMessageCard(
    message: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun ReviewOrderBottomBar(
    isSubmitting: Boolean,
    totalText: String,
    onSubmitOrder: () -> Unit,
    onClearDraft: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryRow(
                label = "Total",
                value = totalText,
                strong = true
            )

            FarmGatePrimaryButton(
                text = if (isSubmitting) "Submitting..." else "Place order",
                onClick = onSubmitOrder,
                enabled = !isSubmitting,
                isLoading = isSubmitting,
                modifier = Modifier.heightIn(min = 52.dp)
            )

            FarmGateSecondaryButton(
                text = "Clear draft",
                onClick = onClearDraft,
                enabled = !isSubmitting,
                modifier = Modifier.heightIn(min = 48.dp)
            )
        }
    }
}

@Composable
private fun QuantityAdjustButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.size(46.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    strong: Boolean = false
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
                fontWeight = if (strong) FontWeight.ExtraBold else FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
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

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value)
    }
}

private fun formatQuantityInput(value: Double, unitType: UnitType): String {
    val wholeNumberOnly = unitType == UnitType.Piece ||
            unitType == UnitType.Dozen ||
            unitType == UnitType.Bundle

    return if (wholeNumberOnly) {
        value.toInt().coerceAtLeast(1).toString()
    } else {
        if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.2f", value)
        }
    }
}