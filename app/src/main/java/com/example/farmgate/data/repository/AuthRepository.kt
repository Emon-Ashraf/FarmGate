package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.datastore.SessionManager
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.AuthSession
import com.example.farmgate.data.remote.api.AuthApi
import com.example.farmgate.data.remote.dto.auth.LoginRequestDto
import com.example.farmgate.data.remote.dto.auth.RegisterRequestDto
import com.example.farmgate.data.remote.mapper.toModel

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(
        login: String,
        password: String
    ): Resource<AuthSession> {
        val result = safeApiCall(
            apiCall = {
                authApi.login(
                    LoginRequestDto(
                        login = login,
                        password = password
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )

        if (result is Resource.Success) {
            sessionManager.saveSession(
                token = result.data.token,
                role = result.data.role.name,
                userId = result.data.userId.toString()
            )
        }

        return result
    }

    suspend fun register(
        fullName: String,
        phoneNumber: String,
        email: String?,
        password: String,
        role: Int,
        primaryCityId: Long?,
        displayName: String?
    ): Resource<AuthSession> {
        val result = safeApiCall(
            apiCall = {
                authApi.register(
                    RegisterRequestDto(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = password,
                        role = role,
                        primaryCityId = primaryCityId,
                        displayName = displayName
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )

        if (result is Resource.Success) {
            sessionManager.saveSession(
                token = result.data.token,
                role = result.data.role.name,
                userId = result.data.userId.toString()
            )
        }

        return result
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
