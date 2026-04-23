package com.example.farmgate.presentation.farmer.profile


import com.example.farmgate.data.model.City
import com.example.farmgate.data.model.UserProfile

data class FarmerProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isLoggingOut: Boolean = false,
    val profile: UserProfile? = null,
    val cities: List<City> = emptyList(),

    val displayName: String = "",
    val description: String = "",
    val selectedCityId: Long? = null,
    val selectedCityName: String? = null,

    val errorMessage: String? = null,
    val successMessage: String? = null
)