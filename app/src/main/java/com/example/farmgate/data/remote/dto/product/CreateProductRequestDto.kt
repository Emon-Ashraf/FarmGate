package com.example.farmgate.data.remote.dto.product


data class CreateProductRequestDto(
    val pickupLocationId: Long,
    val name: String,
    val description: String?,
    val category: String?,
    val unitType: Int,
    val pricePerUnit: Double,
    val availableQuantity: Double,
    val imageUrl: String?
)