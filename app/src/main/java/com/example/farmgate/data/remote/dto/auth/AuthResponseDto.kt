package com.example.farmgate.data.remote.dto.auth

data class AuthResponseDto(
    val accessToken: String,
    val userId: Long,
    val role: Int,
    val fullName: String
)