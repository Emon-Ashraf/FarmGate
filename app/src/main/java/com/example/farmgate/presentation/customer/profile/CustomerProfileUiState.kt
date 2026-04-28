package com.example.farmgate.presentation.customer.profile

import com.example.farmgate.data.model.City
import com.example.farmgate.data.model.UserProfile

data class CustomerProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val cities: List<City> = emptyList(),
    val selectedCityId: Long? = null,
    val selectedCityName: String? = null,
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val actionErrorMessage: String? = null,
    val actionSuccessMessage: String? = null,
    val isLoggingOut: Boolean = false
)