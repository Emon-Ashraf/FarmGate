package com.example.farmgate.presentation.farmer.order


import com.example.farmgate.data.model.Order

data class FarmerOrdersUiState(
    val isLoading: Boolean = true,
    val orders: List<Order> = emptyList(),
    val errorMessage: String? = null
)