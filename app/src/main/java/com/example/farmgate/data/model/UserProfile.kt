package com.example.farmgate.data.model

data class UserProfile(
    val userId: Long,
    val fullName: String,
    val phoneNumber: String,
    val email: String?,
    val primaryCityId: Long?,
    val primaryCityName: String?
)