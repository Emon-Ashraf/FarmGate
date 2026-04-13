package com.example.farmgate.data.model


data class ProductSummary(
    val id: Long,
    val name: String,
    val unitType: UnitType,
    val pricePerUnit: Double,
    val availableQuantity: Double,
    val pickupLocationId: Long,
    val pickupArea: String,
    val cityName: String,
    val farmerName: String,
    val imageUrl: String?
)