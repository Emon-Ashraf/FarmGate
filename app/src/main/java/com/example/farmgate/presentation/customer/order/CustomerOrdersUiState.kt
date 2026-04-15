package com.example.farmgate.presentation.customer.order


import com.example.farmgate.data.model.Order

data class CustomerOrdersUiState(
    val isLoading: Boolean = true,
    val orders: List<Order> = emptyList(),
    val errorMessage: String? = null
)