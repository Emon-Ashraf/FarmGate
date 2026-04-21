package com.example.farmgate.presentation.admin.product


import com.example.farmgate.data.model.AdminProductModerationTarget

data class AdminProductModerationUiState(
    val targets: List<AdminProductModerationTarget> = listOf(
        AdminProductModerationTarget(
            productId = 1,
            productName = "Sample Product",
            farmerName = "Sample Farmer",
            isActive = true
        )
    ),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)