package com.example.farmgate.data.remote.mapper

import com.example.farmgate.data.model.AuthSession
import com.example.farmgate.data.model.RoleType
import com.example.farmgate.data.remote.dto.auth.AuthResponseDto

fun AuthResponseDto.toModel(): AuthSession {
    return AuthSession(
        token = accessToken,
        userId = userId,
        role = RoleType.fromInt(role),
        fullName = fullName
    )
}