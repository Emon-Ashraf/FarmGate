package com.example.farmgate.presentation.customer.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGatePrimaryButton

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RatingTopBar(onBackClick = onBackClick)

            RatingHeroCard(
                score = uiState.score,
                onScoreChanged = onScoreChanged
            )

            RatingCommentCard(
                comment = uiState.comment,
                onCommentChanged = onCommentChanged
            )

            uiState.errorMessage?.let { message ->
                ErrorMessageCard(message = message)
            }

            uiState.successMessage?.let { message ->
                SuccessMessageCard(message = message)
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        RatingBottomBar(
            isSubmitting = uiState.isSubmitting,
            onSubmitClick = onSubmitClick
        )
    }
}

@Composable
private fun RatingTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
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

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Rate farmer",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Share your pickup experience",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RatingHeroCard(
    score: Int,
    onScoreChanged: (Int) -> Unit
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = Color(0x1AF59E0B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "★",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFFF59E0B)
                    )
                }
            }

            Text(
                text = ratingTitle(score),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = ratingSubtitle(score),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            StarRatingSelector(
                selectedScore = score,
                onScoreChanged = onScoreChanged
            )

            Text(
                text = "$score out of 5",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFFF59E0B)
            )
        }
    }
}

@Composable
private fun StarRatingSelector(
    selectedScore: Int,
    onScoreChanged: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { score ->
            val selected = score <= selectedScore

            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onScoreChanged(score) },
                shape = CircleShape,
                color = if (selected) {
                    Color(0x1AF59E0B)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "★",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = if (selected) {
                            Color(0xFFF59E0B)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingCommentCard(
    comment: String,
    onCommentChanged: (String) -> Unit
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
                text = "Add a comment",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Tell other customers about freshness, pickup experience, and farmer communication.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = comment,
                onValueChange = onCommentChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Comment optional") },
                placeholder = { Text("Example: Fresh vegetables and smooth pickup.") },
                minLines = 5,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF18D66B),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@Composable
private fun RatingBottomBar(
    isSubmitting: Boolean,
    onSubmitClick: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FarmGatePrimaryButton(
                text = if (isSubmitting) "Submitting..." else "Submit rating",
                onClick = onSubmitClick,
                enabled = !isSubmitting,
                isLoading = isSubmitting,
                modifier = Modifier.heightIn(min = 52.dp)
            )

            Text(
                text = "Your feedback helps customers choose reliable local farmers.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ErrorMessageCard(
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun SuccessMessageCard(
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0x1A18D66B)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF18D66B)
        )
    }
}

private fun ratingTitle(score: Int): String {
    return when (score) {
        1 -> "Poor experience"
        2 -> "Could be better"
        3 -> "Good enough"
        4 -> "Very good"
        else -> "Excellent pickup"
    }
}

private fun ratingSubtitle(score: Int): String {
    return when (score) {
        1 -> "Tell us what went wrong so it can be improved."
        2 -> "Share what could have made the pickup better."
        3 -> "A fair experience with room for improvement."
        4 -> "Good quality and smooth pickup experience."
        else -> "Fresh products, reliable farmer, and smooth pickup."
    }
}