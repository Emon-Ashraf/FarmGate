package com.example.farmgate.presentation.customer.issue


data class CreateIssueUiState(
    val title: String = "",
    val description: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)