package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.data.remote.api.ProfileApi
import com.example.farmgate.data.remote.dto.profile.UpdateCustomerProfileRequestDto
import com.example.farmgate.data.remote.dto.profile.UpdateFarmerProfileRequestDto
import com.example.farmgate.data.remote.mapper.toModel

class ProfileRepository(
    private val profileApi: ProfileApi
) {
    suspend fun getMyProfile(): Resource<UserProfile> {
        return safeApiCall(
            apiCall = { profileApi.getMyProfile() },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun updateFarmerProfile(
        displayName: String,
        description: String?,
        primaryCityId: Long?
    ): Resource<UserProfile> {
        return safeApiCall(
            apiCall = {
                profileApi.updateFarmerProfile(
                    UpdateFarmerProfileRequestDto(
                        displayName = displayName,
                        description = description,
                        primaryCityId = primaryCityId
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun updateCustomerProfile(
        primaryCityId: Long?
    ): Resource<UserProfile> {
        return safeApiCall(
            apiCall = {
                profileApi.updateCustomerProfile(
                    UpdateCustomerProfileRequestDto(
                        primaryCityId = primaryCityId
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }
}