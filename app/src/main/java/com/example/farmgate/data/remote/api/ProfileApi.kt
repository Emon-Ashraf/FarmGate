package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.profile.ProfileResponseDto
import com.example.farmgate.data.remote.dto.profile.UpdateCustomerProfileRequestDto
import com.example.farmgate.data.remote.dto.profile.UpdateFarmerProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {

    @GET("api/Profile/me")
    suspend fun getMyProfile(): Response<ProfileResponseDto>

    @PUT("api/Profile/farmer")
    suspend fun updateFarmerProfile(
        @Body request: UpdateFarmerProfileRequestDto
    ): Response<ProfileResponseDto>

    @PUT("api/Profile/customer")
    suspend fun updateCustomerProfile(
        @Body request: UpdateCustomerProfileRequestDto
    ): Response<ProfileResponseDto>
}