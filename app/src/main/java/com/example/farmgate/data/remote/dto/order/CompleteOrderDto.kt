package com.example.farmgate.data.remote.dto.order

data class CompleteOrderDto(
    val pickupCode: String,
    val items: List<CompleteOrderItemDto>
)