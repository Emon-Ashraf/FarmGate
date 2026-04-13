package com.example.farmgate.data.model

data class UserProfile(
    val userId: String,
    val fullName: String,
    val phone: String?,
    val email: String?,
    val role: RoleType
)