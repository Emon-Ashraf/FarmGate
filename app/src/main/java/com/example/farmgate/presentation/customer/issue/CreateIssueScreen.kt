package com.example.farmgate.presentation.customer.issue


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateIssueScreen(
    uiState: CreateIssueUiState,
    onBackClick: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
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
            text = "Report Issue",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Issue title") },
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Issue description") },
            minLines = 5
        )

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onSubmitClick,
            enabled = !uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isSubmitting) "Submitting..." else "Submit Issue")
        }
    }
}