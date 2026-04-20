package com.example.farmgate.presentation.customer.rating


data class CreateRatingUiState(
    val score: Int = 5,
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)