package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.pickuplocation.CreatePickupLocationDto
import com.example.farmgate.data.remote.dto.pickuplocation.PickupLocationDto
import com.example.farmgate.data.remote.dto.pickuplocation.UpdatePickupLocationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PickupLocationsApi {

    @GET("api/pickup-locations/me")
    suspend fun getMyPickupLocations(): Response<List<PickupLocationDto>>

    @POST("api/pickup-locations")
    suspend fun createPickupLocation(
        @Body request: CreatePickupLocationDto
    ): Response<PickupLocationDto>

    @PUT("api/pickup-locations/{id}")
    suspend fun updatePickupLocation(
        @Path("id") id: Long,
        @Body request: UpdatePickupLocationDto
    ): Response<PickupLocationDto>

    @DELETE("api/pickup-locations/{id}")
    suspend fun deactivatePickupLocation(
        @Path("id") id: Long
    ): Response<Unit>
}