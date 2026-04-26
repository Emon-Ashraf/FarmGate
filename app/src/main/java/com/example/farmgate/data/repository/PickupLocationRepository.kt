package com.example.farmgate.data.repository

import com.example.farmgate.core.common.Resource
import com.example.farmgate.core.network.safeApiCall
import com.example.farmgate.data.model.PickupLocation
import com.example.farmgate.data.remote.api.PickupLocationsApi
import com.example.farmgate.data.remote.dto.pickuplocation.CreatePickupLocationDto
import com.example.farmgate.data.remote.dto.pickuplocation.UpdatePickupLocationDto
import com.example.farmgate.data.remote.mapper.toModel

class PickupLocationRepository(
    private val pickupLocationsApi: PickupLocationsApi
) {
    suspend fun getMyPickupLocations(): Resource<List<PickupLocation>> {
        return safeApiCall(
            apiCall = { pickupLocationsApi.getMyPickupLocations() },
            mapper = { dtoList -> dtoList.map { it.toModel() } }
        )
    }

    suspend fun createPickupLocation(
        cityId: Long,
        areaName: String,
        addressLine: String,
        latitude: Double?,
        longitude: Double?,
        instructions: String?
    ): Resource<PickupLocation> {
        return safeApiCall(
            apiCall = {
                pickupLocationsApi.createPickupLocation(
                    CreatePickupLocationDto(
                        cityId = cityId,
                        areaName = areaName,
                        addressLine = addressLine,
                        latitude = latitude,
                        longitude = longitude,
                        instructions = instructions
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun updatePickupLocation(
        id: Long,
        cityId: Long,
        areaName: String,
        addressLine: String,
        latitude: Double?,
        longitude: Double?,
        instructions: String?
    ): Resource<PickupLocation> {
        return safeApiCall(
            apiCall = {
                pickupLocationsApi.updatePickupLocation(
                    id = id,
                    request = UpdatePickupLocationDto(
                        cityId = cityId,
                        areaName = areaName,
                        addressLine = addressLine,
                        latitude = latitude,
                        longitude = longitude,
                        instructions = instructions
                    )
                )
            },
            mapper = { dto -> dto.toModel() }
        )
    }

    suspend fun deactivatePickupLocation(id: Long): Resource<Unit> {
        return safeApiCall(
            apiCall = { pickupLocationsApi.deactivatePickupLocation(id) },
            mapper = { Unit }
        )
    }
}