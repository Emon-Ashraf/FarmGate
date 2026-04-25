package com.example.farmgate.presentation.farmer.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    val selectedPickupLocation = uiState.pickupLocations.firstOrNull {
        it.id.toString() == uiState.pickupLocationId
    }

    var pickupExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }

    val unitOptions = listOf(
        1 to "Piece",
        2 to "Kg",
        3 to "Liter",
        4 to "Dozen",
        5 to "Bundle"
    )

    val selectedUnitLabel = unitOptions.firstOrNull { it.first == uiState.unitType }?.second ?: "Piece"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(
            text = if (uiState.isEditMode) "Edit Product" else "Create Product",
            style = MaterialTheme.typography.headlineMedium
        )

        ExposedDropdownMenuBox(
            expanded = pickupExpanded,
            onExpandedChange = { pickupExpanded = !pickupExpanded }
        ) {
            OutlinedTextField(
                value = selectedPickupLocation?.displayName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Pickup Location") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = pickupExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = pickupExpanded,
                onDismissRequest = { pickupExpanded = false }
            ) {
                uiState.pickupLocations.forEach { location ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(location.displayName)
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

        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChanged,
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.category,
            onValueChange = onCategoryChanged,
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = unitExpanded,
            onExpandedChange = { unitExpanded = !unitExpanded }
        ) {
            OutlinedTextField(
                value = selectedUnitLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Unit Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = unitExpanded,
                onDismissRequest = { unitExpanded = false }
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

        OutlinedTextField(
            value = uiState.pricePerUnit,
            onValueChange = onPricePerUnitChanged,
            label = { Text("Price Per Unit") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        OutlinedTextField(
            value = uiState.availableQuantity,
            onValueChange = onAvailableQuantityChanged,
            label = { Text("Available Quantity") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        OutlinedTextField(
            value = uiState.imageUrl,
            onValueChange = onImageUrlChanged,
            label = { Text("Image Url") },
            modifier = Modifier.fillMaxWidth()
        )

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        uiState.successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = onSaveClick,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.isSaving) {
                    if (uiState.isEditMode) "Updating..." else "Creating..."
                } else {
                    if (uiState.isEditMode) "Update Product" else "Create Product"
                }
            )
        }

        if (uiState.isEditMode) {
            Button(
                onClick = onDeactivateClick,
                enabled = !uiState.isDeleting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (uiState.isDeleting) "Deactivating..." else "Deactivate Product"
                )
            }
        }
    }
}