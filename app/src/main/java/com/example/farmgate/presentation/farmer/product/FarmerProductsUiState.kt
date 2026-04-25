package com.example.farmgate.presentation.farmer.product


import com.example.farmgate.data.model.ProductSummary

data class FarmerProductsUiState(
    val isLoading: Boolean = true,
    val products: List<ProductSummary> = emptyList(),
    val errorMessage: String? = null
)