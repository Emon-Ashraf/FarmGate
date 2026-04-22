package com.example.farmgate.presentation.customer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSecondaryButton
import com.example.farmgate.presentation.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomerHomeScreen(
    uiState: CustomerHomeUiState,
    onCitySelected: (Long) -> Unit,
    onRetry: () -> Unit,
    onProductClick: (Long) -> Unit,
    onReviewOrderClick: () -> Unit,
    onMyOrdersClick: () -> Unit
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }

    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = "Loading your local marketplace...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6F7B74)
            )
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.screenErrorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = uiState.screenErrorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Good morning, ${uiState.fullName.ifBlank { "there" }}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Fresh local produce available for pickup in your city.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6F7B74)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Choose city",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = cityDropdownExpanded,
                        onExpandedChange = {
                            cityDropdownExpanded = !cityDropdownExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedCityName.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select a city") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = cityDropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(14.dp),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color(0xFF1A1A1A),
                                unfocusedTextColor = Color(0xFF1A1A1A),
                                focusedBorderColor = Color(0xFF18D66B),
                                unfocusedBorderColor = Color(0xFFD0D0D0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = cityDropdownExpanded,
                            onDismissRequest = { cityDropdownExpanded = false }
                        ) {
                            uiState.cities.forEach { city ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = city.name,
                                            color = Color(0xFF1A1A1A)
                                        )
                                    },
                                    onClick = {
                                        cityDropdownExpanded = false
                                        onCitySelected(city.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmGateSecondaryButton(
                    text = "My Orders",
                    onClick = onMyOrdersClick,
                    enabled = true,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp)
                )

                if (uiState.hasActiveDraft) {
                    FarmGatePrimaryButton(
                        text = "Review Order",
                        onClick = onReviewOrderClick,
                        enabled = true,
                        isLoading = false,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.selectedCityName.isNullOrBlank()) {
                        "Products"
                    } else {
                        "Products in ${uiState.selectedCityName}"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            uiState.isProductsLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.productsErrorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = uiState.productsErrorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }

            uiState.products.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No products found for this city.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6F7B74)
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 18.dp, end = 18.dp, bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }
        }
    }
}