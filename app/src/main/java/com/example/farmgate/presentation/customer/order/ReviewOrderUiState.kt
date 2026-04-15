package com.example.farmgate.presentation.customer.order


import com.example.farmgate.data.model.OrderDraft

data class ReviewOrderUiState(
    val isSubmitting: Boolean = false,
    val draft: OrderDraft? = null,
    val errorMessage: String? = null
)