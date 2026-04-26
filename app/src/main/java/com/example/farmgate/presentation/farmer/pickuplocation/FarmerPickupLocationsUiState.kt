package com.example.farmgate.presentation.farmer.pickuplocation


import com.example.farmgate.data.model.PickupLocation

data class FarmerPickupLocationsUiState(
    val isLoading: Boolean = true,
    val locations: List<PickupLocation> = emptyList(),
    val errorMessage: String? = null
)