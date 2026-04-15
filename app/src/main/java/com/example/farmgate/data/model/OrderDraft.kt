package com.example.farmgate.data.model

data class OrderDraft(
    val pickupLocationId: Long,
    val farmerName: String,
    val pickupArea: String,
    val cityName: String,
    val pickupAddress: String,
    val items: List<OrderDraftItem>
) {
    val estimatedProductTotal: Double
        get() = items.sumOf { it.pricePerUnit * it.selectedQuantity }
}