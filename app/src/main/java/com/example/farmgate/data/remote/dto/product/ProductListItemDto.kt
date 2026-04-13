package com.example.farmgate.data.remote.dto.product


data class ProductListItemDto(
    val id: Long,
    val name: String,
    val unitType: Int,
    val pricePerUnit: Double,
    val availableQuantity: Double,
    val pickupLocationId: Long,
    val pickupArea: String,
    val cityName: String,
    val farmerName: String,
    val imageUrl: String?
)