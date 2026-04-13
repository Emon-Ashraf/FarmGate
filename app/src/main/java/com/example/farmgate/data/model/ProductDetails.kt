package com.example.farmgate.data.model


data class ProductDetails(
    val id: Long,
    val name: String,
    val description: String?,
    val category: String?,
    val unitType: UnitType,
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