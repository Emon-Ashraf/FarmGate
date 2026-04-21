package com.example.farmgate.data.model


data class AdminProductModerationTarget(
    val productId: Long,
    val productName: String,
    val farmerName: String,
    val isActive: Boolean
)