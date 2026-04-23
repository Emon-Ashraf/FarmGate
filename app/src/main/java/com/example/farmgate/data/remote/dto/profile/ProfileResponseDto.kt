package com.example.farmgate.data.remote.dto.profile

data class ProfileResponseDto(
    val userId: Long,
    val fullName: String,
    val phoneNumber: String,
    val email: String?,
    val primaryCityId: Long?,
    val primaryCityName: String?,

    //farmer side extra
    val displayName: String? = null,
    val description: String? = null,
    val isProfileCompleted: Boolean? = null
)