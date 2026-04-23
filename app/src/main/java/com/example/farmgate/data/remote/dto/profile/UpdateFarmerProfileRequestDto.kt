package com.example.farmgate.data.remote.dto.profile


data class UpdateFarmerProfileRequestDto(
    val displayName: String,
    val description: String?,
    val primaryCityId: Long?
)