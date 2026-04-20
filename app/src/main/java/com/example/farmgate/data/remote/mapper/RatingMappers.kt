package com.example.farmgate.data.remote.mapper


import com.example.farmgate.data.model.Rating
import com.example.farmgate.data.remote.dto.rating.RatingDto

fun RatingDto.toModel(): Rating {
    return Rating(
        id = id,
        orderId = orderId,
        customerId = customerId,
        farmerId = farmerId,
        score = score,
        comment = comment,
        customerName = customerName,
        createdAt = createdAt
    )
}