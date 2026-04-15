package com.example.farmgate.data.remote.dto.order

data class OrderItemDto(
    val id: Long,
    val productId: Long,
    val productName: String,
    val unitType: Int,
    val unitPrice: Double,
    val orderedQuantity: Double,
    val fulfilledQuantity: Double?
)