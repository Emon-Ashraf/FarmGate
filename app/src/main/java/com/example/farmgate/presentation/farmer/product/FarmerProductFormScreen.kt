package com.example.farmgate.presentation.farmer.product

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import androidx.compose.foundation.layout.ColumnScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerProductFormScreen(
    uiState: FarmerProductFormUiState,
    onBackClick: () -> Unit,
    onPickupLocationIdChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onUnitTypeChanged: (Int) -> Unit,
    onPricePerUnitChanged: (String) -> Unit,
    onAvailableQuantityChanged: (String) -> Unit,
    onImageUrlChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDeactivateClick: () -> Unit
) {
    var pickupExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    val hasPickupLocations = uiState.pickupLocations.isNotEmpty()

    val categoryOptions = listOf(
        "Vegetables",
        "Fruits",
        "Dairy",
        "Meat",
        "Eggs",
        "Grains",
        "Herbs",
        "Other"
    )

    val unitOptions = listOf(
        1 to "Piece",
        2 to "Kg",
        3 to "Liter",
        4 to "Dozen",
        5 to "Bundle"
    )

    val selectedPickupText = uiState.pickupLocations
        .firstOrNull { it.id.toString() == uiState.pickupLocationId }
        ?.let { "${it.areaName}, ${it.cityName}" }
        ?: ""

    val selectedUnitText = unitOptions
        .firstOrNull { it.first == uiState.unitType }
        ?.second
        ?: "Piece"

    when {
        uiState.isLoading -> {
            FarmerProductFormLoadingState(isEditMode = uiState.isEditMode)
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ProductFormHeader(
                        isEditMode = uiState.isEditMode,
                        onBackClick = onBackClick
                    )

                    ProductImagePreviewCard(
                        imageUrl = uiState.imageUrl,
                        productName = uiState.name.ifBlank {
                            if (uiState.isEditMode) "Product" else "New product"
                        }
                    )

                    SectionCard(
                        title = "Pickup & availability",
                        subtitle = "Choose where customers will collect this product."
                    ) {
                        if (!hasPickupLocations) {
                            PickupRequiredWarning()
                        } else {
                            ExposedDropdownMenuBox(
                                expanded = pickupExpanded,
                                onExpandedChange = { pickupExpanded = !pickupExpanded }
                            ) {
                                FarmGateOutlinedField(
                                    value = selectedPickupText,
                                    onValueChange = {},
                                    label = "Pickup location",
                                    placeholder = "Select pickup location",
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = pickupExpanded
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = pickupExpanded,
                                    onDismissRequest = { pickupExpanded = false },
                                    containerColor = MaterialTheme.colorScheme.surface
                                ) {
                                    uiState.pickupLocations.forEach { location ->
                                        DropdownMenuItem(
                                            text = {
                                                Column(
                                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                                ) {
                                                    Text(
                                                        text = "${location.areaName}, ${location.cityName}",
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = FontWeight.SemiBold
                                                        ),
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )

                                                    Text(
                                                        text = location.addressLine,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            },
                                            onClick = {
                                                onPickupLocationIdChanged(location.id.toString())
                                                pickupExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            HelperText(
                                text = "Customers will collect this product from the selected pickup point."
                            )
                        }

                        ExposedDropdownMenuBox(
                            expanded = unitExpanded,
                            onExpandedChange = { unitExpanded = !unitExpanded }
                        ) {
                            FarmGateOutlinedField(
                                value = selectedUnitText,
                                onValueChange = {},
                                label = "Unit type",
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = unitExpanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = unitExpanded,
                                onDismissRequest = { unitExpanded = false },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                unitOptions.forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            onUnitTypeChanged(value)
                                            unitExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            FarmGateOutlinedField(
                                value = uiState.pricePerUnit,
                                onValueChange = onPricePerUnitChanged,
                                label = "Price",
                                placeholder = "120",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            FarmGateOutlinedField(
                                value = uiState.availableQuantity,
                                onValueChange = onAvailableQuantityChanged,
                                label = "Quantity",
                                placeholder = "15",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    SectionCard(
                        title = "Product information",
                        subtitle = "This is what customers will see in product lists and details."
                    ) {
                        FarmGateOutlinedField(
                            value = uiState.name,
                            onValueChange = onNameChanged,
                            label = "Product name",
                            placeholder = "Fresh tomatoes",
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = !categoryExpanded }
                        ) {
                            FarmGateOutlinedField(
                                value = uiState.category,
                                onValueChange = {},
                                label = "Category",
                                placeholder = "Select category",
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = categoryExpanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                categoryOptions.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category) },
                                        onClick = {
                                            onCategoryChanged(category)
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        FarmGateOutlinedField(
                            value = uiState.description,
                            onValueChange = onDescriptionChanged,
                            label = "Description",
                            placeholder = "Describe freshness, origin, quality, or pickup notes",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            minLines = 4,
                            maxLines = 6
                        )
                    }

                    SectionCard(
                        title = "Product image",
                        subtitle = "For now, image is added using a URL."
                    ) {
                        FarmGateOutlinedField(
                            value = uiState.imageUrl,
                            onValueChange = onImageUrlChanged,
                            label = "Image URL",
                            placeholder = "Paste product image link",
                            modifier = Modifier.fillMaxWidth()
                        )

                        HelperText(
                            text = "Later, this can be replaced with real image upload when backend support is added."
                        )
                    }

                    uiState.errorMessage?.let { message ->
                        MessageCard(
                            message = message,
                            isError = true
                        )
                    }

                    uiState.successMessage?.let { message ->
                        MessageCard(
                            message = message,
                            isError = false
                        )
                    }

                    Spacer(modifier = Modifier.height(110.dp))
                }

                ProductFormBottomBar(
                    isEditMode = uiState.isEditMode,
                    hasPickupLocations = hasPickupLocations,
                    isSaving = uiState.isSaving,
                    isDeleting = uiState.isDeleting,
                    onSaveClick = onSaveClick,
                    onDeactivateClick = onDeactivateClick
                )
            }
        }
    }
}

@Composable
private fun FarmerProductFormLoadingState(
    isEditMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator()

            Text(
                text = if (isEditMode) "Loading product..." else "Preparing form...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductFormHeader(
    isEditMode: Boolean,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = if (isEditMode) "Edit product" else "Create product",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (isEditMode) {
                    "Update product details, price, quantity, and pickup point."
                } else {
                    "Add a product customers can discover and order."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductImagePreviewCard(
    imageUrl: String,
    productName: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                ProductImagePlaceholder(productName = productName)
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
            ) {
                Text(
                    text = "Preview",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ProductImagePlaceholder(
    productName: String
) {
    Surface(
        modifier = Modifier.size(64.dp),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = productName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            content()
        }
    }
}

@Composable
private fun PickupRequiredWarning() {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Pickup location required",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Text(
                text = "You need at least one pickup location before creating a product.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Text(
                text = "Go to Pickup Locations and add one first.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun FarmGateOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        placeholder = placeholder?.let {
            { Text(it) }
        },
        readOnly = readOnly,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF18D66B),
            focusedLabelColor = Color(0xFF18D66B),
            cursorColor = Color(0xFF18D66B)
        )
    )
}

@Composable
private fun HelperText(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun MessageCard(
    message: String,
    isError: Boolean
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color(0x1A18D66B)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
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
private fun ProductFormBottomBar(
    isEditMode: Boolean,
    hasPickupLocations: Boolean,
    isSaving: Boolean,
    isDeleting: Boolean,
    onSaveClick: () -> Unit,
    onDeactivateClick: () -> Unit
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
            FarmGatePrimaryButton(
                text = when {
                    isSaving && isEditMode -> "Updating..."
                    isSaving && !isEditMode -> "Creating..."
                    isEditMode -> "Update product"
                    else -> "Create product"
                },
                onClick = onSaveClick,
                enabled = !isSaving && !isDeleting && hasPickupLocations,
                isLoading = isSaving,
                modifier = Modifier.heightIn(min = 52.dp)
            )

            if (isEditMode) {
                FarmGateSecondaryButton(
                    text = if (isDeleting) "Deactivating..." else "Deactivate product",
                    onClick = onDeactivateClick,
                    enabled = !isSaving && !isDeleting,
                    modifier = Modifier.heightIn(min = 50.dp)
                )
            }
        }
    }
}