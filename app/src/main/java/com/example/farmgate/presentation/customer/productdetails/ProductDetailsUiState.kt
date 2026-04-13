package com.example.farmgate.presentation.customer.productdetails


import com.example.farmgate.data.model.ProductDetails

data class ProductDetailsUiState(
    val isLoading: Boolean = true,
    val product: ProductDetails? = null,
    val errorMessage: String? = null
)