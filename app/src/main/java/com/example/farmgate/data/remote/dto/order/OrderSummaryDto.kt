package com.example.farmgate.data.remote.dto.order

data class OrderSummaryDto(
    val id: Long,
    val status: Int,
    val counterpartyName: String,
    val estimatedProductTotal: Double,
    val actualProductTotal: Double?,
    val serviceFeeAmount: Double,
    val pickupDueAt: String,
    val createdAt: String
)