package com.example.farmgate.data.repository


import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.UserProfile
import com.example.farmgate.data.remote.api.ProfileApi
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
}