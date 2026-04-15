package com.example.farmgate.data.remote.dto.order


data class CreateOrderDto(
    val pickupLocationId: Long,
    val items: List<OrderItemRequestDto>
)