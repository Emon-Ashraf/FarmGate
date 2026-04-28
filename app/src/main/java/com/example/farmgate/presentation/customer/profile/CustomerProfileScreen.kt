package com.example.farmgate.presentation.customer.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton

@Composable
fun CustomerProfileScreen(
    uiState: CustomerProfileUiState,
    onRetry: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onCitySelected: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigation: suspend () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigation()
    }

    when {
        uiState.isLoading -> {
            ProfileLoadingState()
        }

        uiState.errorMessage != null -> {
            ProfileErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry
            )
        }

        uiState.profile != null -> {
            CustomerProfileContent(
                uiState = uiState,
                profile = uiState.profile,
                onRetry = onRetry,
                onEditClick = onEditClick,
                onCancelEditClick = onCancelEditClick,
                onCitySelected = onCitySelected,
                onSaveClick = onSaveClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun ProfileLoadingState() {
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
private fun ProfileErrorState(
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
            text = "My profile",
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
    }
}

@Composable
private fun CustomerProfileContent(
    uiState: CustomerProfileUiState,
    profile: UserProfile,
    onRetry: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onCitySelected: (Long) -> Unit,
    onSaveClick: () -> Unit,
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
        ProfileHeaderTitle()

        ProfileHeroCard(profile = profile)

        ProfileInfoCard(
            title = "Account information",
            rows = listOf(
                ProfileInfoRowData(
                    label = "Full name",
                    value = profile.fullName.ifBlank { "Not provided" }
                ),
                ProfileInfoRowData(
                    label = "Phone",
                    value = profile.phoneNumber.ifBlank { "Not provided" }
                ),
                ProfileInfoRowData(
                    label = "Email",
                    value = profile.email?.takeIf { it.isNotBlank() } ?: "Not provided"
                )
            )
        )

        ProfileLocationCard(
            uiState = uiState,
            onEditClick = onEditClick,
            onCancelEditClick = onCancelEditClick,
            onCitySelected = onCitySelected,
            onSaveClick = onSaveClick
        )

        uiState.actionErrorMessage?.let { message ->
            MessageCard(
                message = message,
                isError = true
            )
        }

        uiState.actionSuccessMessage?.let { message ->
            MessageCard(
                message = message,
                isError = false
            )
        }

        ProfileHelpCard()

        ProfileActionsCard(
            isLoggingOut = uiState.isLoggingOut,
            isSaving = uiState.isSaving,
            onRetry = onRetry,
            onLogoutClick = onLogoutClick
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun ProfileHeaderTitle() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "My profile",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Manage your FarmGate customer account",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileHeroCard(
    profile: UserProfile
) {
    val initials = profile.initials()
    val cityText = profile.primaryCityName?.takeIf { it.isNotBlank() } ?: "No city selected"

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
                    text = profile.fullName.ifBlank { "Customer" },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Customer account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileChip(text = "Customer")
                ProfileChip(text = cityText)
            }
        }
    }
}

@Composable
private fun ProfileChip(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
private fun ProfileInfoCard(
    title: String,
    rows: List<ProfileInfoRowData>
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
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            rows.forEachIndexed { index, row ->
                ProfileInfoRow(row = row)

                if (index != rows.lastIndex) {
                    ProfileDivider()
                }
            }
        }
    }
}

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
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProfileLocationCard(
    uiState: CustomerProfileUiState,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit,
    onCitySelected: (Long) -> Unit,
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
                    text = "Location",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!uiState.isEditMode) {
                    Text(
                        text = "Edit",
                        modifier = Modifier.clickable(onClick = onEditClick),
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
                        label = "Primary city",
                        value = uiState.profile?.primaryCityName?.takeIf { it.isNotBlank() }
                            ?: "Not selected yet"
                    )
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                .clickable { cityMenuExpanded = true },
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
                                color = MaterialTheme.colorScheme.onSurface
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FarmGateSecondaryButton(
                            text = "Cancel",
                            onClick = onCancelEditClick,
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                        )

                        FarmGatePrimaryButton(
                            text = if (uiState.isSaving) "Saving..." else "Save",
                            onClick = onSaveClick,
                            enabled = !uiState.isSaving,
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
private fun ProfileHelpCard() {
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
                    text = "Currently, customers can update only their primary city. Name, phone, email, and profile photo require backend support.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileActionsCard(
    isLoggingOut: Boolean,
    isSaving: Boolean,
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
                modifier = Modifier.heightIn(min = 50.dp),
            )
        }
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
    val result = fullName
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    return result.ifBlank { "U" }
}