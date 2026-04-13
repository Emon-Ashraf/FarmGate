package com.example.farmgate.data.model

data class AuthSession(
    val token: String,
    val userId: Long,
    val role: RoleType,
    val fullName: String
)