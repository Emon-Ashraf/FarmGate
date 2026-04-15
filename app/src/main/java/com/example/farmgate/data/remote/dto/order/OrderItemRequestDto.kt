package com.example.farmgate.data.remote.dto.order


data class OrderItemRequestDto(
    val productId: Long,
    val quantity: Double
)