package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.profile.ProfileResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {

    @GET("api/Profile/me")
    suspend fun getMyProfile(): Response<ProfileResponseDto>
}