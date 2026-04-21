package com.example.farmgate.presentation.admin.issue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.IssueStatus

@Composable
fun AdminIssueDetailsScreen(
    uiState: AdminIssueDetailsUiState,
    onBackClick: () -> Unit,
    onStatusChanged: (IssueStatus) -> Unit,
    onAdminNoteChanged: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(
            text = "Admin Issue Details",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Issue #${uiState.issueId}")
                Text(text = "Order #${uiState.orderId}")
                Text(text = "Title: ${uiState.title}")
                Text(text = "Current Status: ${uiState.status.name}")
                Text(text = "Customer: ${uiState.customerName}")
                Text(text = "Farmer: ${uiState.farmerName}")
                Text(text = "Created At: ${uiState.createdAt}")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Update Status",
                    style = MaterialTheme.typography.titleMedium
                )

                IssueStatus.entries.forEach { status ->
                    Button(
                        onClick = { onStatusChanged(status) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (uiState.selectedStatus == status) {
                                "Selected: ${status.name}"
                            } else {
                                status.name
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.adminNote,
                    onValueChange = onAdminNoteChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Admin note (optional)") },
                    minLines = 4
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
                    onClick = onSubmitClick,
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (uiState.isSubmitting) "Updating..." else "Update Issue"
                    )
                }
            }
        }
    }
}