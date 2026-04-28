package com.example.farmgate.presentation.farmer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton

@Composable
fun FarmerProfileScreen(
    uiState: FarmerProfileUiState,
    onDisplayNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCitySelected: (Long) -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRetry: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    when {
        uiState.isLoading -> {
            FarmerProfileLoadingState()
        }

        uiState.errorMessage != null && uiState.profile == null -> {
            FarmerProfileErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry
            )
        }

        uiState.profile != null -> {
            FarmerProfileContent(
                uiState = uiState,
                profile = uiState.profile,
                onDisplayNameChanged = onDisplayNameChanged,
                onDescriptionChanged = onDescriptionChanged,
                onCitySelected = onCitySelected,
                onEditClick = onEditClick,
                onCancelEditClick = onCancelEditClick,
                onSaveClick = onSaveClick,
                onRetry = onRetry,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun FarmerProfileLoadingState() {
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
                text = "Loading farmer profile...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FarmerProfileErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Farmer profile",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "We could not load your profile.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

                FarmGatePrimaryButton(
                    text = "Retry",
                    onClick = onRetry,
                    enabled = true,
                    isLoading = false
                )
            }
        }
    }
}

@Composable
private fun FarmerProfileContent(
    uiState: FarmerProfileUiState,
    profile: UserProfile,
    onDisplayNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCitySelected: (Long) -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRetry: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FarmerProfileHeader()

        FarmerProfileHeroCard(
            profile = profile,
            displayName = uiState.displayName,
            selectedCityName = uiState.selectedCityName
        )

        if (!profile.isProfileCompleted) {
            ProfileCompletionWarningCard()
        }

        PublicFarmerInfoCard(
            uiState = uiState,
            profile = profile,
            onDisplayNameChanged = onDisplayNameChanged,
            onDescriptionChanged = onDescriptionChanged,
            onCitySelected = onCitySelected,
            onEditClick = onEditClick,
            onCancelEditClick = onCancelEditClick,
            onSaveClick = onSaveClick
        )

        AccountInfoCard(profile = profile)

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

        FarmerProfileHelpCard()

        FarmerProfileActionsCard(
            isSaving = uiState.isSaving,
            isLoggingOut = uiState.isLoggingOut,
            onRetry = onRetry,
            onLogoutClick = onLogoutClick
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun FarmerProfileHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Farmer profile",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Manage your public farm identity and pickup city",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FarmerProfileHeroCard(
    profile: UserProfile,
    displayName: String,
    selectedCityName: String?
) {
    val initials = profile.initials()
    val title = displayName.ifBlank {
        profile.displayName?.takeIf { it.isNotBlank() }
            ?: profile.fullName.ifBlank { "Farmer" }
    }
    val cityText = selectedCityName?.takeIf { it.isNotBlank() }
        ?: profile.primaryCityName?.takeIf { it.isNotBlank() }
        ?: "No city selected"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(88.dp),
                shape = CircleShape,
                color = Color(0x1A18D66B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = profile.fullName.ifBlank { "Farmer account" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FarmerProfileChip(
                    text = if (profile.isProfileCompleted) "Completed" else "Incomplete",
                    positive = profile.isProfileCompleted
                )

                FarmerProfileChip(
                    text = cityText,
                    positive = profile.primaryCityName != null
                )
            }
        }
    }
}

@Composable
private fun ProfileCompletionWarningCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0x1AF97316)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = Color(0x26F97316)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "!",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFFF97316)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Complete your farmer profile",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFB45309)
                )

                Text(
                    text = "Add a display name and primary city so your products and pickup locations are clearer for customers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB45309)
                )
            }
        }
    }
}

@Composable
private fun PublicFarmerInfoCard(
    uiState: FarmerProfileUiState,
    profile: UserProfile,
    onDisplayNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCitySelected: (Long) -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var cityMenuExpanded by remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Public farmer information",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!uiState.isEditMode) {
                    Text(
                        text = "Edit",
                        modifier = Modifier.clickable(
                            enabled = !uiState.isSaving && !uiState.isLoggingOut,
                            onClick = onEditClick
                        ),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }

            if (!uiState.isEditMode) {
                ProfileInfoRow(
                    row = ProfileInfoRowData(
                        label = "Display name",
                        value = profile.displayName?.takeIf { it.isNotBlank() }
                            ?: "Not provided"
                    )
                )

                ProfileDivider()

                ProfileInfoRow(
                    row = ProfileInfoRowData(
                        label = "Description",
                        value = profile.description?.takeIf { it.isNotBlank() }
                            ?: "Not provided"
                    )
                )

                ProfileDivider()

                ProfileInfoRow(
                    row = ProfileInfoRowData(
                        label = "Primary city",
                        value = profile.primaryCityName?.takeIf { it.isNotBlank() }
                            ?: "Not selected yet"
                    )
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FarmGateOutlinedField(
                        value = uiState.displayName,
                        onValueChange = onDisplayNameChanged,
                        label = "Display name",
                        placeholder = "Example: Green Valley Farms",
                        enabled = !uiState.isSaving && !uiState.isLoggingOut,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    FarmGateOutlinedField(
                        value = uiState.description,
                        onValueChange = onDescriptionChanged,
                        label = "Description",
                        placeholder = "Describe your farm, products, freshness, or pickup style",
                        enabled = !uiState.isSaving && !uiState.isLoggingOut,
                        minLines = 4,
                        maxLines = 6,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Primary city",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Box {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = !uiState.isSaving && !uiState.isLoggingOut
                                    ) {
                                        cityMenuExpanded = true
                                    },
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                            ) {
                                Text(
                                    text = uiState.selectedCityName?.takeIf { it.isNotBlank() }
                                        ?: "Choose city",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            DropdownMenu(
                                expanded = cityMenuExpanded,
                                onDismissRequest = { cityMenuExpanded = false },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                uiState.cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = city.name,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            cityMenuExpanded = false
                                            onCitySelected(city.id)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = "Customers see this farmer information on product and order screens.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FarmGateSecondaryButton(
                            text = "Cancel",
                            onClick = onCancelEditClick,
                            enabled = !uiState.isSaving && !uiState.isLoggingOut,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                        )

                        FarmGatePrimaryButton(
                            text = if (uiState.isSaving) "Saving..." else "Save",
                            onClick = onSaveClick,
                            enabled = !uiState.isSaving && !uiState.isLoggingOut,
                            isLoading = uiState.isSaving,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountInfoCard(
    profile: UserProfile
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Account information",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            ProfileInfoRow(
                row = ProfileInfoRowData(
                    label = "Full name",
                    value = profile.fullName.ifBlank { "Not provided" }
                )
            )

            ProfileDivider()

            ProfileInfoRow(
                row = ProfileInfoRowData(
                    label = "Phone",
                    value = profile.phoneNumber.ifBlank { "Not provided" }
                )
            )

            ProfileDivider()

            ProfileInfoRow(
                row = ProfileInfoRowData(
                    label = "Email",
                    value = profile.email?.takeIf { it.isNotBlank() } ?: "Not provided"
                )
            )
        }
    }
}

@Composable
private fun FarmerProfileHelpCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
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
                color = Color(0x1A18D66B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "i",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF18D66B)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Profile editing",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Currently, farmers can update display name, description, and primary city. Profile photo requires backend upload support.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FarmerProfileActionsCard(
    isSaving: Boolean,
    isLoggingOut: Boolean,
    onRetry: () -> Unit,
    onLogoutClick: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Account actions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            FarmGateSecondaryButton(
                text = "Refresh profile",
                onClick = onRetry,
                enabled = !isLoggingOut && !isSaving,
                modifier = Modifier.heightIn(min = 50.dp)
            )

            FarmGatePrimaryButton(
                text = if (isLoggingOut) "Logging out..." else "Logout",
                onClick = onLogoutClick,
                enabled = !isLoggingOut && !isSaving,
                isLoading = isLoggingOut,
                modifier = Modifier.heightIn(min = 50.dp)
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
private fun FarmerProfileChip(
    text: String,
    positive: Boolean
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (positive) {
            Color(0x1A18D66B)
        } else {
            Color(0x1AF97316)
        }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = if (positive) {
                Color(0xFF18D66B)
            } else {
                Color(0xFFF97316)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private data class ProfileInfoRowData(
    val label: String,
    val value: String
)

@Composable
private fun ProfileInfoRow(
    row: ProfileInfoRowData
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = row.label,
            modifier = Modifier.weight(0.9f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = row.value,
            modifier = Modifier.weight(1.2f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MessageCard(
    message: String,
    isError: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = if (isError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color(0x1A18D66B)
        }
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
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
private fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    )
}

private fun UserProfile.initials(): String {
    val source = displayName
        ?.takeIf { it.isNotBlank() }
        ?: fullName

    val result = source
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    return result.ifBlank { "F" }
}