package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.Rating
import com.example.farmgate.data.remote.api.RatingsApi
import com.example.farmgate.data.remote.dto.rating.CreateRatingDto
import com.example.farmgate.data.remote.mapper.toModel

class RatingRepository(
    private val ratingsApi: RatingsApi
) {

    suspend fun createRating(
        orderId: Long,
        score: Int,
        comment: String?
    ): Resource<Rating> {
        return safeApiCall(
            apiCall = {
                ratingsApi.createRating(
                    CreateRatingDto(
                        orderId = orderId,
                        score = score,
                        comment = comment
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun getRatingsForFarmer(
        farmerId: Long
    ): Resource<List<Rating>> {
        return safeApiCall(
            apiCall = { ratingsApi.getRatingsForFarmer(farmerId) },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }
}