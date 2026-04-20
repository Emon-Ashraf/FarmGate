package com.example.farmgate.data.remote.api


import com.example.farmgate.data.remote.dto.rating.CreateRatingDto
import com.example.farmgate.data.remote.dto.rating.RatingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingsApi {

    @POST("api/ratings")
    suspend fun createRating(
        @Body request: CreateRatingDto
    ): Response<RatingDto>

    @GET("api/ratings/farmers/{farmerId}")
    suspend fun getRatingsForFarmer(
        @Path("farmerId") farmerId: Long
    ): Response<List<RatingDto>>
}