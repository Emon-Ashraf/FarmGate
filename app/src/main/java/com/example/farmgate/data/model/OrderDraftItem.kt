package com.example.farmgate.data.model


data class OrderDraftItem(
    val productId: Long,
    val productName: String,
    val unitType: UnitType,
    val pricePerUnit: Double,
    val availableQuantity: Double,
    val selectedQuantity: Double,
    val pickupLocationId: Long
)