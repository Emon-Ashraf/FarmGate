package com.example.farmgate.presentation.farmer.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

    val selectedPickupText = uiState.pickupLocations
        .firstOrNull { it.id.toString() == uiState.pickupLocationId }
        ?.let { "${it.areaName}, ${it.cityName}" }
        ?: ""

    val selectedUnitText = when (uiState.unitType) {
        1 -> "Piece"
        2 -> "Kg"
        3 -> "Liter"
        4 -> "Dozen"
        5 -> "Bundle"
        else -> "Piece"
    }

    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (uiState.isEditMode) "Edit Product" else "Create Product",
                style = MaterialTheme.typography.headlineMedium
            )
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (uiState.isEditMode) "Edit Product" else "Create Product",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (uiState.isEditMode) {
                    "Update your product information for customers."
                } else {
                    "Add a new product for customers to discover."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SectionCard(title = "Pickup & Availability") {
            ExposedDropdownMenuBox(
                expanded = pickupExpanded,
                onExpandedChange = { pickupExpanded = !pickupExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPickupText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pickup Location") },
                    placeholder = { Text("Select pickup location") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = pickupExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = pickupExpanded,
                    onDismissRequest = { pickupExpanded = false }
                ) {
                    uiState.pickupLocations.forEach { location ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("${location.areaName}, ${location.cityName}")
                                    Text(
                                        text = location.addressLine,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Customers will collect this product from the selected pickup point.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = unitExpanded,
                onExpandedChange = { unitExpanded = !unitExpanded }
            ) {
                OutlinedTextField(
                    value = selectedUnitText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Unit Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = unitExpanded,
                    onDismissRequest = { unitExpanded = false }
                ) {
                    listOf(
                        1 to "Piece",
                        2 to "Kg",
                        3 to "Liter",
                        4 to "Dozen",
                        5 to "Bundle"
                    ).forEach { (value, label) ->
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.pricePerUnit,
                onValueChange = onPricePerUnitChanged,
                label = { Text("Price Per Unit") },
                placeholder = { Text("e.g. 120") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.availableQuantity,
                onValueChange = onAvailableQuantityChanged,
                label = { Text("Available Quantity") },
                placeholder = { Text("e.g. 15") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        SectionCard(title = "Product Information") {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChanged,
                label = { Text("Product Name") },
                placeholder = { Text("e.g. Fresh Tomatoes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    placeholder = { Text("Select category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChanged,
                label = { Text("Description") },
                placeholder = { Text("Describe freshness, origin, or special notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                minLines = 4,
                maxLines = 6
            )
        }

        SectionCard(title = "Product Image") {
            OutlinedTextField(
                value = uiState.imageUrl,
                onValueChange = onImageUrlChanged,
                label = { Text("Image URL") },
                placeholder = { Text("Paste product image link") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "For now, image is added using a URL. Later we can replace this with real image upload.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        uiState.errorMessage?.let {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        uiState.successMessage?.let {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0x1A18D66B),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = Color(0xFF18D66B),
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = onSaveClick,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = when {
                    uiState.isSaving && uiState.isEditMode -> "Updating..."
                    uiState.isSaving && !uiState.isEditMode -> "Creating..."
                    uiState.isEditMode -> "Update Product"
                    else -> "Create Product"
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        if (uiState.isEditMode) {
            OutlinedButton(
                onClick = onDeactivateClick,
                enabled = !uiState.isDeleting,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = if (uiState.isDeleting) {
                        "Deactivating..."
                    } else {
                        "Deactivate Product"
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}