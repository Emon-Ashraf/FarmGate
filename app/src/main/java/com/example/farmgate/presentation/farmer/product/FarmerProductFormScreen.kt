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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

        OutlinedTextField(
            value = uiState.pickupLocationId,
            onValueChange = onPickupLocationIdChanged,
            label = { Text("Pickup Location Id") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

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

        OutlinedTextField(
            value = uiState.unitType.toString(),
            onValueChange = { value -> onUnitTypeChanged(value.toIntOrNull() ?: 1) },
            label = { Text("Unit Type (1/2/3...)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

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

        Text(
            text = "Temporary developer note: unit type and pickup location id are manual for now.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}