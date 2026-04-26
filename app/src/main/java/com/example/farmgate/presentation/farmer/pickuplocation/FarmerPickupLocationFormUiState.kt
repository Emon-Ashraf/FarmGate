package com.example.farmgate.presentation.farmer.pickuplocation

import com.example.farmgate.data.model.City

data class FarmerPickupLocationFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isEditMode: Boolean = false,

    val cities: List<City> = emptyList(),
    val selectedCityId: String = "",

    val areaName: String = "",
    val addressLine: String = "",
    val instructions: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null
)