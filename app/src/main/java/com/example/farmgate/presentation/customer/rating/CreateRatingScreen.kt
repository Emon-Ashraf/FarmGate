package com.example.farmgate.presentation.customer.rating


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
fun CreateRatingScreen(
    uiState: CreateRatingUiState,
    onBackClick: () -> Unit,
    onScoreChanged: (Int) -> Unit,
    onCommentChanged: (String) -> Unit,
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
            text = "Rate Farmer",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(text = "Score (1 to 5): ${uiState.score}")

        RowScoreSelector(
            selectedScore = uiState.score,
            onScoreChanged = onScoreChanged
        )

        OutlinedTextField(
            value = uiState.comment,
            onValueChange = onCommentChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Comment (optional)") },
            minLines = 4
        )

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onSubmitClick,
            enabled = !uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isSubmitting) "Submitting..." else "Submit Rating")
        }
    }
}



@Composable
private fun RowScoreSelector(
    selectedScore: Int,
    onScoreChanged: (Int) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (1..5).forEach { score ->
            Button(onClick = { onScoreChanged(score) }) {
                Text(score.toString())
            }
        }
    }
}