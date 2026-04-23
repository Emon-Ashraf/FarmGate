package com.example.farmgate.data.remote.mapper

import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.data.remote.dto.profile.ProfileResponseDto

fun ProfileResponseDto.toModel(): UserProfile {
    return UserProfile(
        userId = userId,
        fullName = fullName,
        phoneNumber = phoneNumber,
        email = email,
        primaryCityId = primaryCityId,
        primaryCityName = primaryCityName,
        displayName = displayName,
        description = description,
        isProfileCompleted = isProfileCompleted ?: false
    )
}