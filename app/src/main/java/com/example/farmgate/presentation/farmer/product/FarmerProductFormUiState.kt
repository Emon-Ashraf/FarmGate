package com.example.farmgate.presentation.farmer.product

import com.example.farmgate.data.model.PickupLocation

data class FarmerProductFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isEditMode: Boolean = false,

    val pickupLocationId: String = "",
    val pickupLocations: List<PickupLocation> = emptyList(),

    val name: String = "",
    val description: String = "",
    val category: String = "",
    val unitType: Int = 1,
    val pricePerUnit: String = "",
    val availableQuantity: String = "",
    val imageUrl: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null
)