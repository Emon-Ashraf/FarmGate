package com.example.farmgate.presentation.farmer.order

import com.example.farmgate.data.model.Order

data class FarmerOrderDetailsUiState(
    val isLoading: Boolean = true,
    val order: Order? = null,
    val errorMessage: String? = null,
    val isAccepting: Boolean = false,
    val isRejecting: Boolean = false,
    val isCompleting: Boolean = false,
    val pickupCode: String = "",
    val fulfilledQuantities: Map<Long, String> = emptyMap(),
    val actionErrorMessage: String? = null,
    val actionSuccessMessage: String? = null
)