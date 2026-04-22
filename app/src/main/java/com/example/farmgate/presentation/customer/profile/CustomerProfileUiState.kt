package com.example.farmgate.presentation.customer.profile


import com.example.farmgate.data.model.UserProfile

data class CustomerProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val errorMessage: String? = null,
    val isLoggingOut: Boolean = false
)