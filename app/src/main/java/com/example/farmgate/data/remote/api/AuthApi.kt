package com.example.farmgate.data.remote.api

import com.example.farmgate.data.remote.dto.auth.AuthResponseDto
import com.example.farmgate.data.remote.dto.auth.LoginRequestDto
import com.example.farmgate.data.remote.dto.auth.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/Auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<AuthResponseDto>

    @POST("api/Auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<AuthResponseDto>
}