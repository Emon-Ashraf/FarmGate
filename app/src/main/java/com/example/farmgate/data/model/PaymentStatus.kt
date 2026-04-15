package com.example.farmgate.data.model

enum class PaymentStatus {
    Pending,
    Paid,
    Failed,
    Refunded;

    companion object {
        fun fromInt(value: Int): PaymentStatus {
            return when (value) {
                1 -> Pending
                2 -> Paid
                3 -> Failed
                4 -> Refunded
                else -> Pending
            }
        }
    }
}