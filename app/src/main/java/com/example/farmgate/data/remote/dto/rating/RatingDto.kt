package com.example.farmgate.data.remote.dto.rating


data class RatingDto(
    val id: Long,
    val orderId: Long,
    val customerId: Long,
    val farmerId: Long,
    val score: Int,
    val comment: String?,
    val customerName: String?,
    val createdAt: String
)