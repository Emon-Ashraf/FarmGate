package com.example.farmgate.data.model

enum class CancellationReason {
    CustomerCancelled,
    PaymentTimeout,
    FarmerUnavailable,
    CustomerNoShow,
    AdminCancelled;

    companion object {
        fun fromInt(value: Int): CancellationReason {
            return when (value) {
                1 -> CustomerCancelled
                2 -> PaymentTimeout
                3 -> FarmerUnavailable
                4 -> CustomerNoShow
                5 -> AdminCancelled
                else -> CustomerCancelled
            }
        }
    }
}