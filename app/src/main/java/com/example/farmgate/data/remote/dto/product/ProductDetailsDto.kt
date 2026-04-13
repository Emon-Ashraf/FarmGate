package com.example.farmgate.data.remote.dto.product


data class ProductDetailsDto(
    val id: Long,
    val name: String,
    val description: String?,
    val category: String?,
    val unitType: Int,
    val pricePerUnit: Double,
    val availableQuantity: Double,
    val pickupLocationId: Long,
    val pickupArea: String,
    val cityName: String,
    val pickupAddress: String,
    val instructions: String?,
    val farmerName: String,
    val farmerPhone: String?,
    val imageUrl: String?
)