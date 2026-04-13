package com.example.farmgate.data.remote.mapper

import com.example.farmgate.data.model.RoleType
import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.data.remote.dto.profile.ProfileResponseDto

fun ProfileResponseDto.toModel(): UserProfile {
    return UserProfile(
        userId = userId,
        fullName = fullName,
        phone = phone,
        email = email,
        role = RoleType.fromString(role)
    )
}