package com.example.farmgate.presentation.admin.user


import com.example.farmgate.data.model.AdminUserModerationTarget

data class AdminUserModerationUiState(
    val targets: List<AdminUserModerationTarget> = listOf(
        AdminUserModerationTarget(
            userId = 1,
            displayName = "Sample Customer",
            roleName = "Customer",
            isActive = true
        ),
        AdminUserModerationTarget(
            userId = 2,
            displayName = "Sample Farmer",
            roleName = "Farmer",
            isActive = true
        )
    ),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)