package com.example.farmgate.presentation.admin.user


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminUserModerationScreen(
    uiState: AdminUserModerationUiState,
    onDeactivateUser: (Long) -> Unit,
    onActivateUser: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Admin User Moderation",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        uiState.errorMessage?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        uiState.successMessage?.let {
            item {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(uiState.targets) { user ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = user.displayName, style = MaterialTheme.typography.titleMedium)
                    Text(text = "User ID: ${user.userId}")
                    Text(text = "Role: ${user.roleName}")
                    Text(text = "Active: ${user.isActive}")

                    if (user.isActive) {
                        Button(
                            onClick = { onDeactivateUser(user.userId) },
                            enabled = !uiState.isSubmitting,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Deactivate User")
                        }
                    } else {
                        Button(
                            onClick = { onActivateUser(user.userId) },
                            enabled = !uiState.isSubmitting,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Activate User")
                        }
                    }
                }
            }
        }
    }
}