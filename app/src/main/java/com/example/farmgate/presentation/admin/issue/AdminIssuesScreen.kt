package com.example.farmgate.presentation.admin.issue


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.farmgate.data.model.AdminIssueListItem

@Composable
fun AdminIssuesScreen(
    uiState: AdminIssuesUiState,
    onRetry: () -> Unit,
    onIssueClick: (AdminIssueListItem) -> Unit
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Admin Issues",
                    style = MaterialTheme.typography.headlineMedium
                )
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Admin Issues",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Open Issues",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                if (uiState.issues.isEmpty()) {
                    item {
                        Text(
                            text = "No open issues found.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(uiState.issues) { issue ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onIssueClick(issue) }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = issue.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(text = "Issue #${issue.id}")
                                Text(text = "Order #${issue.orderId}")
                                Text(text = "Status: ${issue.status.name}")
                                Text(text = "Customer: ${issue.customerName}")
                                Text(text = "Farmer: ${issue.farmerName}")
                                Text(text = "Created At: ${issue.createdAt}")
                            }
                        }
                    }
                }
            }
        }
    }
}