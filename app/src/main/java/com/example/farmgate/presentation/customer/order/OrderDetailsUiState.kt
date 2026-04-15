package com.example.farmgate.presentation.customer.order


import com.example.farmgate.data.model.Order

data class OrderDetailsUiState(
    val isLoading: Boolean = true,
    val order: Order? = null,
    val errorMessage: String? = null
)