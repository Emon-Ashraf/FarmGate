package com.example.farmgate.data.remote.dto.auth

data class RegisterRequestDto(
    val fullName: String,
    val phoneNumber: String,
    val email: String?,
    val password: String,
    val role: Int,
    val primaryCityId: Long? = null,
    val displayName: String? = null
)