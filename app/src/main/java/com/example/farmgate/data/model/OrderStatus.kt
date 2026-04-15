package com.example.farmgate.data.model


enum class OrderStatus {
    Pending,
    AwaitingFee,
    Confirmed,
    Completed,
    Rejected,
    Cancelled;

    companion object {
        fun fromInt(value: Int): OrderStatus {
            return when (value) {
                1 -> Pending
                2 -> AwaitingFee
                3 -> Confirmed
                4 -> Completed
                5 -> Rejected
                6 -> Cancelled
                else -> Pending
            }
        }
    }
}