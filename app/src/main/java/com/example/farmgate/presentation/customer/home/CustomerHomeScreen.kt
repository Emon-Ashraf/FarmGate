package com.example.farmgate.presentation.customer.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGateSearchBar
import com.example.farmgate.presentation.components.ProductCard

@Composable
fun CustomerHomeScreen(
    uiState: CustomerHomeUiState,
    onCitySelected: (Long) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val screenBackground = Color.White

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.screenErrorMessage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(screenBackground)
                .padding(18.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "FarmGate",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        contentPadding = PaddingValues(
            start = 4.dp,
            end = 4.dp,
            top = 4.dp,
            bottom = if (uiState.hasActiveDraft) 92.dp else 18.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            CustomerHomeHeader(
                uiState = uiState,
                cityDropdownExpanded = cityDropdownExpanded,
                onCityDropdownClick = {
                    cityDropdownExpanded = true
                },
                onCityDropdownDismiss = {
                    cityDropdownExpanded = false
                },
                onCitySelected = { cityId ->
                    cityDropdownExpanded = false
                    onCitySelected(cityId)
                },
                onSearchQueryChanged = onSearchQueryChanged
            )
        }

        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Text(
                text = if (uiState.selectedCityName.isNullOrBlank()) {
                    if (uiState.searchQuery.isBlank()) "Products" else "Search results"
                } else {
                    if (uiState.searchQuery.isBlank()) {
                        "Products in ${uiState.selectedCityName}"
                    } else {
                        "Results in ${uiState.selectedCityName}"
                    }
                },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        when {
            uiState.isProductsLoading -> {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            uiState.productsErrorMessage != null -> {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
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
            }

            uiState.products.isEmpty() -> {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        text = if (uiState.searchQuery.isBlank()) {
                            "No products found for this city."
                        } else {
                            "No products matched your search."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
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

@Composable
private fun CustomerHomeHeader(
    uiState: CustomerHomeUiState,
    cityDropdownExpanded: Boolean,
    onCityDropdownClick: () -> Unit,
    onCityDropdownDismiss: () -> Unit,
    onCitySelected: (Long) -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomEnd = 8.dp, bottomStart = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_farmgate_small),
                        contentDescription = "FarmGate",
                        modifier = Modifier.size(42.dp)
                    )

                    Text(
                        text = "FarmGate",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                }

                Box {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        shadowElevation = 2.dp,
                        onClick = onCityDropdownClick
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = "Location",
                                tint = Color(0xFFF50153),
                                modifier = Modifier.size(16.dp)
                            )

                            Text(
                                text = uiState.selectedCityName?.takeIf { it.isNotBlank() }
                                    ?: "Choose city",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                contentDescription = "Select city",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = cityDropdownExpanded,
                        onDismissRequest = onCityDropdownDismiss,
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
                                    onCitySelected(city.id)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Good morning, ${uiState.fullName.ifBlank { "there" }}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            FarmGateSearchBar(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged
            )
        }
    }
}