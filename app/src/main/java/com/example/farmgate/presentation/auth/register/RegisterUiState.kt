package com.example.farmgate.presentation.auth.register

data class RegisterUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val role: Int = 1,
    val primaryCityId: Long? = null,
    val displayName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)