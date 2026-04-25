package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.pickuplocation.PickupLocationDto
import retrofit2.Response
import retrofit2.http.GET

interface PickupLocationsApi {

    @GET("api/pickup-locations/me")
    suspend fun getMyPickupLocations(): Response<List<PickupLocationDto>>
}