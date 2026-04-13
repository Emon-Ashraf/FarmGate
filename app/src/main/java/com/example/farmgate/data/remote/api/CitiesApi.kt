package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.city.CityResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi {

    @GET("api/Cities")
    suspend fun getCities(): Response<List<CityResponseDto>>
}