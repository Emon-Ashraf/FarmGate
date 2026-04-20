package com.example.farmgate.data.remote.dto.rating


data class CreateRatingDto(
    val orderId: Long,
    val score: Int,
    val comment: String?
)