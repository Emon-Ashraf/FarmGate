package com.example.farmgate.presentation.farmer.pickuplocation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.farmgate.presentation.components.FarmGateLabeledTextField
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerPickupLocationFormScreen(
    uiState: FarmerPickupLocationFormUiState,
    onBackClick: () -> Unit,
    onCityChanged: (String) -> Unit,
    onAreaNameChanged: (String) -> Unit,
    onAddressLineChanged: (String) -> Unit,
    onInstructionsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDeactivateClick: () -> Unit
) {
    var cityExpanded by remember { mutableStateOf(false) }

    val selectedCityText = uiState.cities
        .firstOrNull { it.id.toString() == uiState.selectedCityId }
        ?.name
        .orEmpty()

    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (uiState.isEditMode) "Edit Pickup Location" else "Create Pickup Location",
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(
            text = if (uiState.isEditMode) "Edit Pickup Location" else "Create Pickup Location",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Add the place where customers will collect their order.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "City",
            style = MaterialTheme.typography.titleMedium
        )

        ExposedDropdownMenuBox(
            expanded = cityExpanded,
            onExpandedChange = { cityExpanded = !cityExpanded }
        ) {
            OutlinedTextField(
                value = selectedCityText,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select city") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = cityExpanded,
                onDismissRequest = { cityExpanded = false }
            ) {
                uiState.cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city.name) },
                        onClick = {
                            onCityChanged(city.id.toString())
                            cityExpanded = false
                        }
                    )
                }
            }
        }

        FarmGateLabeledTextField(
            label = "Area / Neighborhood",
            value = uiState.areaName,
            onValueChange = onAreaNameChanged,
            placeholder = "Example: Dhanmondi 27",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            enabled = !uiState.isSaving && !uiState.isDeleting
        )

        FarmGateLabeledTextField(
            label = "Pickup address",
            value = uiState.addressLine,
            onValueChange = onAddressLineChanged,
            placeholder = "Example: House 12, Road 7, near Green Market",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            enabled = !uiState.isSaving && !uiState.isDeleting
        )

        FarmGateLabeledTextField(
            label = "Extra instructions (optional)",
            value = uiState.instructions,
            onValueChange = onInstructionsChanged,
            placeholder = "Example: Call before arrival or collect from front gate",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            enabled = !uiState.isSaving && !uiState.isDeleting
        )

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        uiState.successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FarmGatePrimaryButton(
            text = if (uiState.isEditMode) "Update Pickup Location" else "Create Pickup Location",
            onClick = onSaveClick,
            enabled = !uiState.isSaving && !uiState.isDeleting,
            isLoading = uiState.isSaving
        )

        if (uiState.isEditMode) {
            FarmGateSecondaryButton(
                text = if (uiState.isDeleting) "Deactivating..." else "Deactivate Pickup Location",
                onClick = onDeactivateClick,
                enabled = !uiState.isSaving && !uiState.isDeleting
            )
        }
    }
}