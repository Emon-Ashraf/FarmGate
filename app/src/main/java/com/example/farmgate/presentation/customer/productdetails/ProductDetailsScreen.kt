package com.example.farmgate.presentation.customer.productdetails

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.farmgate.R
import com.example.farmgate.data.model.ProductDetails
import com.example.farmgate.data.model.UnitType
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import java.util.Locale
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProductDetailsScreen(
    uiState: ProductDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onOrderQuantityChanged: (String) -> Unit,
    onAddToOrderClick: () -> Unit,
    onReviewOrderClick: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    when {
        uiState.isLoading -> {
            ProductDetailsLoadingState()
        }

        uiState.errorMessage != null -> {
            ProductDetailsErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry,
                onBackClick = onBackClick
            )
        }

        uiState.product != null -> {
            ProductDetailsContent(
                uiState = uiState,
                onBackClick = onBackClick,
                onOrderQuantityChanged = onOrderQuantityChanged,
                onAddToOrderClick = onAddToOrderClick,
                onReviewOrderClick = onReviewOrderClick
            )
        }
    }
}

@Composable
private fun ProductDetailsLoadingState() {
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
private fun ProductDetailsErrorState(
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
            text = "Product Details",
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
private fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    onBackClick: () -> Unit,
    onOrderQuantityChanged: (String) -> Unit,
    onAddToOrderClick: () -> Unit,
    onReviewOrderClick: () -> Unit
) {
    val product = uiState.product ?: return
    val unitLabel = product.unitType.toDisplayLabel()
    val priceText = formatNumber(product.pricePerUnit)
    val availableText = formatNumber(product.availableQuantity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProductImageHeader(
                    product = product,
                    onBackClick = onBackClick
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 250.dp),
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        SheetHandle()

                        ProductMainInfo(
                            product = product,
                            unitLabel = unitLabel,
                            priceText = priceText,
                            availableText = availableText
                        )

                        if (!product.description.isNullOrBlank()) {
                            ProductDescriptionText(
                                description = product.description
                            )
                        }

                        DetailDivider()

                        FarmerCard(product = product)

                        PickupLocationCard(product = product)

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }

        AddToOrderBottomBar(
            uiState = uiState,
            unitType = product.unitType,
            unitLabel = unitLabel,
            onOrderQuantityChanged = onOrderQuantityChanged,
            onAddToOrderClick = onAddToOrderClick,
            onReviewOrderClick = onReviewOrderClick
        )
    }
}

@Composable
private fun ProductImageHeader(
    product: ProductDetails,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(285.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (!product.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)
                ) {
                    Text(
                        text = product.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
            shadowElevation = 3.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ProductMainInfo(
    product: ProductDetails,
    unitLabel: String,
    priceText: String,
    availableText: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = product.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "BDT $priceText",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF18D66B),
                    maxLines = 1
                )

                Text(
                    text = "per $unitLabel",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoChip(text = "In stock")

            InfoChip(text = "Pickup only")

            if (!product.category.isNullOrBlank()) {
                InfoChip(text = product.category)
            }
        }

        Text(
            text = "Available: $availableText $unitLabel",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProductDescriptionText(
    description: String
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun InfoChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DetailDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    )
}

@Composable
private fun AddToOrderBottomBar(
    uiState: ProductDetailsUiState,
    unitType: UnitType,
    unitLabel: String,
    onOrderQuantityChanged: (String) -> Unit,
    onAddToOrderClick: () -> Unit,
    onReviewOrderClick: () -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuantityAdjustButton(
                    text = "−",
                    onClick = {
                        val current = uiState.orderQuantity.toDoubleOrNull() ?: 1.0
                        val next = if (current > 1.0) current - 1.0 else 1.0
                        onOrderQuantityChanged(formatQuantityInput(next, unitType))
                    }
                )

                OutlinedTextField(
                    value = uiState.orderQuantity,
                    onValueChange = onOrderQuantityChanged,
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
                        val current = uiState.orderQuantity.toDoubleOrNull() ?: 1.0
                        val next = current + 1.0
                        onOrderQuantityChanged(formatQuantityInput(next, unitType))
                    }
                )
            }

            uiState.orderErrorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.infoMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF18D66B)
                )
            }

            FarmGatePrimaryButton(
                text = "Add to order",
                onClick = onAddToOrderClick,
                enabled = !uiState.isDraftActionRunning,
                isLoading = uiState.isDraftActionRunning,
                modifier = Modifier.heightIn(min = 50.dp)
            )

            if (uiState.hasActiveDraft) {
                FarmGateSecondaryButton(
                    text = "Review order",
                    onClick = onReviewOrderClick,
                    enabled = !uiState.isDraftActionRunning,
                    modifier = Modifier.heightIn(min = 48.dp)
                )
            }
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

@Composable
private fun FarmerAvatar(
    name: String
) {
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "F"

    Surface(
        modifier = Modifier.size(48.dp),
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
}

private fun openDialer(
    context: android.content.Context,
    phoneNumber: String
) {
    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$phoneNumber")
    )
    context.startActivity(intent)
}

private fun openMapForAddress(
    context: android.content.Context,
    address: String
) {
    val encodedAddress = Uri.encode(address)
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:0,0?q=$encodedAddress")
    )
    context.startActivity(intent)
}

@Composable
private fun SheetHandle() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(999.dp)
                )
        )
    }
}


@Composable
private fun FarmerCard(
    product: ProductDetails
) {
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FarmerAvatar(name = product.farmerName)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = product.farmerName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.farmerPhone ?: "Phone not provided",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!product.farmerPhone.isNullOrBlank()) {
                Surface(
                    onClick = {
                        openDialer(
                            context = context,
                            phoneNumber = product.farmerPhone
                        )
                    },
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = Color(0x1A18D66B)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = "Call farmer",
                            tint = Color(0xFF18D66B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PickupLocationCard(
    product: ProductDetails
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pickup Location",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Directions",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF18D66B),
                modifier = Modifier.clickable {
                    openMapForAddress(
                        context = context,
                        address = "${product.pickupAddress}, ${product.pickupArea}, ${product.cityName}"
                    )
                }
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pickup area",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Pickup location",
                        tint = Color(0xFFF50153),
                        modifier = Modifier.size(20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "${product.pickupArea}, ${product.cityName}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = product.pickupAddress,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (!product.instructions.isNullOrBlank()) {
                            Text(
                                text = product.instructions,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF18D66B)
                            )
                        }
                    }
                }
            }
        }
    }
}