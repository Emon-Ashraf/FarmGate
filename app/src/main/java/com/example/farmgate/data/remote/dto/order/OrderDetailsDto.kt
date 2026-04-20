package com.example.farmgate.data.remote.dto.order

import com.example.farmgate.data.remote.dto.payment.PaymentDto

data class OrderDetailsDto(
    val id: Long,
    val status: Int,
    val cancellationReason: Int?,
    val customerName: String,
    val farmerName: String,
    val estimatedProductTotal: Double,
    val actualProductTotal: Double?,
    val serviceFeeAmount: Double,
    val feePaidAt: String?,
    val pickupDueAt: String,
    val pickupCity: String,
    val pickupArea: String,
    val pickupAddress: String,
    val pickupInstructions: String?,
    val farmerPhone: String?,
    val pickupCode: String?,
    val createdAt: String,
    val items: List<OrderItemDto>,
    val payment: PaymentDto?
)