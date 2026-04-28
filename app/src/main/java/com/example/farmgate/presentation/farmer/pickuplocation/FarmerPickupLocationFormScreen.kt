package com.example.farmgate.presentation.farmer.pickuplocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
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

    when {
        uiState.isLoading -> {
            PickupLocationFormLoadingState(isEditMode = uiState.isEditMode)
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
                    PickupLocationFormHeader(
                        isEditMode = uiState.isEditMode,
                        onBackClick = onBackClick
                    )

                    PickupLocationHeroCard(
                        selectedCityText = selectedCityText,
                        areaName = uiState.areaName,
                        addressLine = uiState.addressLine
                    )

                    SectionCard(
                        title = "Pickup point",
                        subtitle = "This is the place where customers will collect their order."
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = cityExpanded,
                            onExpandedChange = { cityExpanded = !cityExpanded }
                        ) {
                            FarmGateOutlinedField(
                                value = selectedCityText,
                                onValueChange = {},
                                label = "City",
                                placeholder = "Select city",
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = cityExpanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = cityExpanded,
                                onDismissRequest = { cityExpanded = false },
                                containerColor = MaterialTheme.colorScheme.surface
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

                        FarmGateOutlinedField(
                            value = uiState.areaName,
                            onValueChange = onAreaNameChanged,
                            label = "Area / neighborhood",
                            placeholder = "Example: Dhanmondi 27",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            enabled = !uiState.isSaving && !uiState.isDeleting,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        FarmGateOutlinedField(
                            value = uiState.addressLine,
                            onValueChange = onAddressLineChanged,
                            label = "Pickup address",
                            placeholder = "Example: House 12, Road 7, near Green Market",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            enabled = !uiState.isSaving && !uiState.isDeleting,
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3
                        )
                    }

                    SectionCard(
                        title = "Extra instructions",
                        subtitle = "Optional note to help customers find the pickup point."
                    ) {
                        FarmGateOutlinedField(
                            value = uiState.instructions,
                            onValueChange = onInstructionsChanged,
                            label = "Instructions",
                            placeholder = "Example: Call before arrival or collect from front gate",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            enabled = !uiState.isSaving && !uiState.isDeleting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            minLines = 3,
                            maxLines = 5
                        )

                        HelperText(
                            text = "Keep this simple and useful. Example: gate number, shop name, or meeting point."
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

                PickupLocationFormBottomBar(
                    isEditMode = uiState.isEditMode,
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
private fun PickupLocationFormLoadingState(
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
                text = if (isEditMode) {
                    "Loading pickup location..."
                } else {
                    "Preparing form..."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PickupLocationFormHeader(
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
                text = if (isEditMode) "Edit pickup location" else "Create pickup location",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (isEditMode) {
                    "Update where customers collect orders."
                } else {
                    "Add a place where customers will collect orders."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PickupLocationHeroCard(
    selectedCityText: String,
    areaName: String,
    addressLine: String
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
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                shape = CircleShape,
                color = Color(0x1A18D66B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Pickup location",
                        tint = Color(0xFF18D66B),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = areaName.ifBlank { "Pickup area" },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = selectedCityText.ifBlank { "Select city" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = addressLine.ifBlank { "Address will appear here" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
private fun FarmGateOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
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
        enabled = enabled,
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
private fun PickupLocationFormBottomBar(
    isEditMode: Boolean,
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
                    isEditMode -> "Update pickup location"
                    else -> "Create pickup location"
                },
                onClick = onSaveClick,
                enabled = !isSaving && !isDeleting,
                isLoading = isSaving,
                modifier = Modifier.heightIn(min = 52.dp)
            )

            if (isEditMode) {
                FarmGateSecondaryButton(
                    text = if (isDeleting) "Deactivating..." else "Deactivate pickup location",
                    onClick = onDeactivateClick,
                    enabled = !isSaving && !isDeleting,
                    modifier = Modifier.heightIn(min = 50.dp)
                )
            }
        }
    }
}