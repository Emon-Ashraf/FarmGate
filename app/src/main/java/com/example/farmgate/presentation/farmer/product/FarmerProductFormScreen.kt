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
import com.example.farmgate.data.model.UnitType

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

    val selectedUnit = UnitType.fromApiValue(uiState.unitType)
    var unitMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(
            text = if (uiState.isEditMode) "Edit Product" else "Create Product",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = if (uiState.isEditMode) {
                "Update product information for your listing."
            } else {
                "Add a new product for customers to discover."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = uiState.pickupLocationId,
            onValueChange = onPickupLocationIdChanged,
            label = { Text("Pickup location ID") },
            supportingText = {
                Text("Temporary for now. Later this will be a pickup-location selector.")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChanged,
            label = { Text("Product name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            label = { Text("Description") },
            placeholder = { Text("Fresh organic tomatoes from local farm") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = uiState.category,
            onValueChange = onCategoryChanged,
            label = { Text("Category") },
            placeholder = { Text("Vegetables, Fruits, Dairy...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = unitMenuExpanded,
            onExpandedChange = { unitMenuExpanded = !unitMenuExpanded }
        ) {
            OutlinedTextField(
                value = selectedUnit.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Unit type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitMenuExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = unitMenuExpanded,
                onDismissRequest = { unitMenuExpanded = false }
            ) {
                UnitType.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit.displayName) },
                        onClick = {
                            onUnitTypeChanged(unit.apiValue)
                            unitMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.pricePerUnit,
            onValueChange = onPricePerUnitChanged,
            label = { Text("Price per unit") },
            placeholder = { Text("100") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            supportingText = {
                Text("Example: price for 1 ${selectedUnit.displayName.lowercase()}")
            }
        )

        OutlinedTextField(
            value = uiState.availableQuantity,
            onValueChange = onAvailableQuantityChanged,
            label = { Text("Available quantity") },
            placeholder = { Text("25") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            supportingText = {
                Text("How many ${selectedUnit.displayName.lowercase()} are available")
            }
        )

        OutlinedTextField(
            value = uiState.imageUrl,
            onValueChange = onImageUrlChanged,
            label = { Text("Image URL") },
            placeholder = { Text("https://example.com/image.jpg") },
            supportingText = {
                Text("Optional for now")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
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

        Text(
            text = "Next improvement: replace pickup location ID with a real pickup-location dropdown.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}