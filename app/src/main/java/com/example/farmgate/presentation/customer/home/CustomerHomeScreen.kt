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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.material3.Icon
import com.example.farmgate.R
import com.example.farmgate.presentation.components.FarmGatePrimaryButton
import com.example.farmgate.presentation.components.FarmGateSearchBar
import com.example.farmgate.presentation.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    uiState: CustomerHomeUiState,
    onCitySelected: (Long) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onProductClick: (Long) -> Unit,
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val screenBackground = Color(0xFFFFFFFF)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
            //color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 18.dp, top = 12.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_farmgate_small),
                                contentDescription = "FarmGate",
                                modifier = Modifier.size(42.dp),
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

                    }

                    ExposedDropdownMenuBox(
                        expanded = cityDropdownExpanded,
                        onExpandedChange = {
                            cityDropdownExpanded = !cityDropdownExpanded
                        }
                    ) {
                        Surface(
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            shadowElevation = 2.dp,
                            onClick = { cityDropdownExpanded = true }
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
                                    text = uiState.selectedCityName?.takeIf { it.isNotBlank() } ?: "Choose city",
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

                        ExposedDropdownMenu(
                            expanded = cityDropdownExpanded,
                            onDismissRequest = { cityDropdownExpanded = false },
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
                                        cityDropdownExpanded = false
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

                //Spacer(modifier = Modifier.height(4.dp))

                /*Text(
                    text = "Find fresh produce near you.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )*/

                Spacer(modifier = Modifier.height(8.dp))

                FarmGateSearchBar(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChanged
                )


                Spacer(modifier = Modifier.height(12.dp))

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
        }

        when {
            uiState.isProductsLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 18.dp, end = 18.dp, bottom = 18.dp, top = 12.dp),
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