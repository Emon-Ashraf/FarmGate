package com.example.farmgate.data.remote.dto.profile


data class ProfileResponseDto(
    val userId: String,
    val fullName: String,
    val phone: String?,
    val email: String?,
    val role: String
)