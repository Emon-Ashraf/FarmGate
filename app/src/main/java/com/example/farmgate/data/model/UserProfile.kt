package com.example.farmgate.data.model

data class UserProfile(
    val userId: Long,
    val fullName: String,
    val phoneNumber: String,
    val email: String?,
    val primaryCityId: Long?,
    val primaryCityName: String?,

    //farmer side extras
    val displayName: String? = null,
    val description: String? = null,
    val isProfileCompleted: Boolean = false
)