package com.example.farmgate.presentation.customer.productdetails

import com.example.farmgate.data.model.ProductDetails

data class ProductDetailsUiState(
    val isLoading: Boolean = true,
    val product: ProductDetails? = null,
    val orderQuantity: String = "1",
    val isDraftActionRunning: Boolean = false,
    val errorMessage: String? = null,
    val orderErrorMessage: String? = null,
    val infoMessage: String? = null,
    val hasActiveDraft: Boolean = false
)