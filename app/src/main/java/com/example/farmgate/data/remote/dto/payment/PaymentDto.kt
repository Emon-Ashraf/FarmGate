package com.example.farmgate.data.remote.dto.payment

data class PaymentDto(
    val id: Long,
    val orderId: Long,
    val amount: Double,
    val status: Int,
    val transactionReference: String?,
    val paidAt: String?
)