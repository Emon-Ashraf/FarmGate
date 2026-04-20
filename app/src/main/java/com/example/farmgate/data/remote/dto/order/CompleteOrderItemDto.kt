package com.example.farmgate.data.remote.dto.order


data class CompleteOrderItemDto(
    val orderItemId: Long,
    val fulfilledQuantity: Double
)