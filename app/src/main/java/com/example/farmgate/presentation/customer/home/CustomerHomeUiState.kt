package com.example.farmgate.presentation.customer.home

import com.example.farmgate.data.model.City
import com.example.farmgate.data.model.ProductSummary

data class CustomerHomeUiState(
    val isLoading: Boolean = false,
    val isProductsLoading: Boolean = false,
    val fullName: String = "",
    val cities: List<City> = emptyList(),
    val selectedCityId: Long? = null,
    val selectedCityName: String? = null,
    val searchQuery: String = "",
    val products: List<ProductSummary> = emptyList(),
    val screenErrorMessage: String? = null,
    val productsErrorMessage: String? = null,
    val hasActiveDraft: Boolean = false
)