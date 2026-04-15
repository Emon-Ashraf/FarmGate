package com.example.farmgate.data.model

data class Order(
    val id: Long,
    val status: OrderStatus,
    val cancellationReason: CancellationReason? = null,

    val counterpartyName: String? = null,
    val customerName: String? = null,
    val farmerName: String? = null,

    val estimatedProductTotal: Double,
    val actualProductTotal: Double?,
    val serviceFeeAmount: Double,

    val feePaidAt: String? = null,
    val pickupDueAt: String,
    val pickupCity: String? = null,
    val pickupArea: String? = null,
    val pickupAddress: String? = null,
    val pickupInstructions: String? = null,
    val farmerPhone: String? = null,

    val createdAt: String,
    val items: List<OrderItem> = emptyList(),
    val payment: Payment? = null
)